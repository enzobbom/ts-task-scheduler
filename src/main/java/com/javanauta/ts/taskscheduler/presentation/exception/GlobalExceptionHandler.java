package com.javanauta.ts.taskscheduler.presentation.exception;

import com.javanauta.ts.taskscheduler.domain.exception.BusinessValidationException;
import com.javanauta.ts.taskscheduler.domain.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // business exceptions

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handlerResourceNotFoundException(ResourceNotFoundException ex) {
        log.warn("Resource not found exception: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<String> handlerValidationErrorException(BusinessValidationException ex) {
        log.warn("Invalid value exception: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(ex.getMessage());
    }

    // Input validation exceptions

    // Handles validation errors from @Valid annotations in controller methods
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body("Validation failed: " + details);
    }

    // Handles JSON parsing errors, such as malformed JSON or type mismatches
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body("Malformed JSON or invalid data type: " + ex.getMostSpecificCause().getMessage());
    }

    // Generic error handling

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
    }
}
