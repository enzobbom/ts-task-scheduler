package com.javanauta.ts.taskscheduler.service.exception;

public class ValidationErrorException extends RuntimeException {
    public ValidationErrorException(String message) {
        super(message);
    }
    public ValidationErrorException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
