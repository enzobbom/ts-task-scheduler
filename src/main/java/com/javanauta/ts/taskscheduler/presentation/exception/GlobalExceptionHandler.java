package com.javanauta.ts.taskscheduler.presentation.exception;

import com.javanauta.ts.taskscheduler.application.exception.ServiceValidationException;
import com.javanauta.ts.taskscheduler.application.exception.enums.ServiceExceptionCode;
import com.javanauta.ts.taskscheduler.domain.exception.enums.DomainExceptionCode;
import com.javanauta.ts.taskscheduler.presentation.exception.enums.PresentationExceptionCode;
import com.javanauta.ts.taskscheduler.presentation.exception.enums.PresentationValidationExceptionCode;
import com.javanauta.ts.taskscheduler.presentation.response.ErrorResponse;
import com.javanauta.ts.taskscheduler.presentation.response.FieldErrorDetail;
import com.javanauta.ts.taskscheduler.shared.exception.ApplicationException;
import com.javanauta.ts.taskscheduler.shared.exception.ExceptionCode;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final Map<ExceptionCode, HttpStatus> BUSINESS_CODE_HTTP_STATUS_MAP = Map.of(
            ServiceExceptionCode.TASK_NOT_FOUND, HttpStatus.NOT_FOUND,
            ServiceExceptionCode.NO_TASK_OWNERSHIP, HttpStatus.FORBIDDEN,
            DomainExceptionCode.DOMAIN_VALIDATION_ERROR, HttpStatus.UNPROCESSABLE_CONTENT);

    private static final Map<ExceptionCode, HttpStatus> PRESENTATION_CODE_HTTP_STATUS_MAP = Map.of(
            PresentationValidationExceptionCode.REQUEST_BODY_VALIDATION_ERROR, HttpStatus.UNPROCESSABLE_CONTENT,
            PresentationValidationExceptionCode.REQUEST_PARAM_VALIDATION_ERROR, HttpStatus.UNPROCESSABLE_CONTENT,
            PresentationExceptionCode.TYPE_MISMATCH_ERROR, HttpStatus.BAD_REQUEST,
            PresentationExceptionCode.JSON_PARSE_ERROR, HttpStatus.BAD_REQUEST,
            PresentationExceptionCode.MISSING_PARAMETER_ERROR, HttpStatus.BAD_REQUEST,
            PresentationExceptionCode.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);

    // Input validation exceptions: @Valid and @Validated

    // RequestBody DTO validations (annotated with @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();

        ExceptionCode exceptionCode = PresentationValidationExceptionCode.REQUEST_BODY_VALIDATION_ERROR;
        HttpStatus httpCode = PRESENTATION_CODE_HTTP_STATUS_MAP.get(exceptionCode);
        String errorCode = exceptionCode.getCode();
        ErrorResponse errorResponse;

        if (bindingResult.hasFieldErrors()) {
            // Attribute validation

            List<FieldErrorDetail> fieldErrors = ex.getFieldErrors()
                    .stream()
                    .map(fieldError -> new FieldErrorDetail(
                            fieldError.getField(),
                            fieldError.getDefaultMessage()))
                    .toList();

            errorResponse = new ErrorResponse(
                    httpCode,
                    errorCode,
                    ex.getMessage(),
                    fieldErrors);

        } else {
            // Object validation (e.g., class-level constraints)

            List<FieldErrorDetail> fieldErrors = ex.getAllErrors()
                    .stream()
                    .map(fieldError -> new FieldErrorDetail(
                            "class-level",
                            fieldError.getDefaultMessage()))
                    .toList();

            errorResponse = new ErrorResponse(
                    httpCode,
                    errorCode,
                    ex.getMessage(),
                    fieldErrors);
        }

        return ResponseEntity.status(httpCode).body(errorResponse);
    }

    // Endpoint parameters validation (validation annotation at RequestParam and PathVariable)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        ExceptionCode exceptionCode = PresentationValidationExceptionCode.REQUEST_PARAM_VALIDATION_ERROR;
        HttpStatus httpCode = PRESENTATION_CODE_HTTP_STATUS_MAP.get(exceptionCode);
        String errorCode = exceptionCode.getCode();

        List<FieldErrorDetail> fieldErrors = ex.getConstraintViolations()
                .stream()
                .map(fieldError -> new FieldErrorDetail(
                        fieldError.getPropertyPath().toString(),
                        fieldError.getMessage()))
                .toList();

        ErrorResponse errorResponse = new ErrorResponse(
                httpCode,
                errorCode,
                ex.getMessage(),
                fieldErrors);

        return ResponseEntity.status(httpCode).body(errorResponse);
    }

    // Input validation exceptions: type mismatches and missing parameters

    // Handles type mismatch errors, such as when a parameter cannot be converted to the expected type
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        ExceptionCode exceptionCode = PresentationExceptionCode.TYPE_MISMATCH_ERROR;
        HttpStatus httpCode = PRESENTATION_CODE_HTTP_STATUS_MAP.get(exceptionCode);
        String errorCode = exceptionCode.getCode();
        String message = "Invalid value for parameter: " + ex.getName();

        ErrorResponse errorResponse = new ErrorResponse(
                httpCode,
                errorCode,
                message,
                List.of());

        return ResponseEntity.status(httpCode).body(errorResponse);
    }

    // Handles JSON parsing errors, such as malformed JSON or type mismatches
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ExceptionCode exceptionCode = PresentationExceptionCode.JSON_PARSE_ERROR;
        HttpStatus httpCode = PRESENTATION_CODE_HTTP_STATUS_MAP.get(exceptionCode);
        String errorCode = exceptionCode.getCode();

        ErrorResponse errorResponse = new ErrorResponse(
                httpCode,
                errorCode,
                exceptionCode.getDefaultMessage(),
                List.of());

        return ResponseEntity.status(httpCode).body(errorResponse);
    }

    // Handles missing required parameters in request
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        ExceptionCode exceptionCode = PresentationExceptionCode.MISSING_PARAMETER_ERROR;
        HttpStatus httpCode = PRESENTATION_CODE_HTTP_STATUS_MAP.get(exceptionCode);
        String errorCode = exceptionCode.getCode();
        String message = "Missing required parameter: " + ex.getParameterName();

        ErrorResponse errorResponse = new ErrorResponse(
                httpCode,
                errorCode,
                message,
                List.of());

        return ResponseEntity.status(httpCode).body(errorResponse);
    }

    //

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException ex) {
        ExceptionCode exceptionCode = ex.getCode();
        HttpStatus httpCode = BUSINESS_CODE_HTTP_STATUS_MAP.get(exceptionCode);

        List<FieldErrorDetail> fieldErrors = ex.getFieldExceptionDetails()
                .stream()
                .map(fieldException -> new FieldErrorDetail(
                        fieldException.field(),
                        fieldException.message()))
                .toList();

        ErrorResponse errorResponse = new ErrorResponse(
                httpCode,
                exceptionCode.getCode(),
                ex.getMessage(),
                fieldErrors);

        return ResponseEntity.status(httpCode).body(errorResponse);
    }

    // Service exceptions

    // To be removed as it will be used internally only
    @ExceptionHandler(ServiceValidationException.class)
    public ResponseEntity<ErrorResponse> handlerServiceValidationException(ServiceValidationException ex) {
        ExceptionCode exceptionCode = PresentationExceptionCode.INTERNAL_SERVER_ERROR;
        HttpStatus httpCode = PRESENTATION_CODE_HTTP_STATUS_MAP.get(exceptionCode);
        String errorCode = exceptionCode.getCode();

        ErrorResponse errorResponse = new ErrorResponse(
                httpCode,
                errorCode,
                ex.getMessage(),
                List.of());

        return ResponseEntity.status(httpCode).body(errorResponse);
    }

    // Generic error handling

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unhandled exception", ex);

        ExceptionCode exceptionCode = PresentationExceptionCode.INTERNAL_SERVER_ERROR;
        HttpStatus httpCode = PRESENTATION_CODE_HTTP_STATUS_MAP.get(exceptionCode);
        String errorCode = exceptionCode.getCode();

        ErrorResponse errorResponse = new ErrorResponse(
                httpCode,
                errorCode,
                exceptionCode.getDefaultMessage(),
                List.of());

        return ResponseEntity.status(httpCode).body(errorResponse);
    }
}
