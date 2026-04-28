package com.javanauta.ts.taskscheduler.application.exception;

public class ServiceValidationException extends RuntimeException {
    public ServiceValidationException(String message) {
        super(message);
    }
    public ServiceValidationException(String message, Throwable throwable) { super(message, throwable); }
}
