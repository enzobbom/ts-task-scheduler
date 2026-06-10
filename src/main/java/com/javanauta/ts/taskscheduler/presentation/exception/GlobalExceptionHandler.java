package com.javanauta.ts.taskscheduler.presentation.exception;

import com.javanauta.ts.taskscheduler.application.exception.ForbiddenException;
import com.javanauta.ts.taskscheduler.application.exception.ResourceNotFoundException;
import com.javanauta.ts.taskscheduler.application.exception.ServiceValidationException;
import com.javanauta.ts.taskscheduler.domain.exception.BusinessValidationException;
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
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Input validation exceptions: @Valid and @Validated

    // RequestBody DTO validations (annotated with @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        BindingResult bindingResult = ex.getBindingResult();
        String details;

        if (bindingResult.hasFieldErrors()) {
            // Attribute validation

            details = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
        } else {
            // Object validation (e.g., class-level constraints)

            details = ex.getAllErrors().stream()
                    .map(error -> error.getObjectName() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
        }

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(details);
    }

    // Endpoint parameters validation (validation annotation at RequestParam and PathVariable)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex) {

        List<String> details = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .toList();

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(String.join(", ", details));
    }

    // Input validation exceptions: type mismatches and missing parameters

    // Handles type mismatch errors, such as when a parameter cannot be converted to the expected type
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid value for parameter: " + ex.getName());
    }

    // Handles JSON parsing errors, such as malformed JSON or type mismatches
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Malformed JSON or invalid data type");
    }

    // Handles missing required parameters in request
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing required parameter: " + ex.getParameterName());
    }

    // Service exceptions

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handlerResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<String> handlerForbiddenException(ForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(ServiceValidationException.class)
    public ResponseEntity<String> handlerServiceValidationException(ServiceValidationException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(ex.getMessage());
    }

    // Domain exception

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<String> handlerBusinessValidationException(BusinessValidationException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(ex.getMessage());
    }

    // Generic error handling

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
    }
}
