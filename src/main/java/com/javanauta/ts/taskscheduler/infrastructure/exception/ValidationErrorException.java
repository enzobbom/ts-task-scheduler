package com.javanauta.ts.taskscheduler.infrastructure.exception;

public class ValidationErrorException extends RuntimeException {
    public ValidationErrorException(String message) {
        super(message);
    }
    public ValidationErrorException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
