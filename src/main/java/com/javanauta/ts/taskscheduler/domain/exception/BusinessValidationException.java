package com.javanauta.ts.taskscheduler.domain.exception;

public class BusinessValidationException extends RuntimeException {
    public BusinessValidationException(String message) {
        super(message);
    }
    public BusinessValidationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
