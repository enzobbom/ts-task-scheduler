package com.javanauta.ts.taskscheduler.infrastructure.exception;

public class InvalidTimeZoneException extends RuntimeException {
    public InvalidTimeZoneException(String message) {
        super(message);
    }
    public InvalidTimeZoneException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
