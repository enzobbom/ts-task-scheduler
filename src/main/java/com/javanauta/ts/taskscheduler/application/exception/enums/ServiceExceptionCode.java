package com.javanauta.ts.taskscheduler.application.exception.enums;

import com.javanauta.ts.taskscheduler.shared.exception.ExceptionCode;

public enum ServiceExceptionCode implements ExceptionCode {
    TASK_NOT_FOUND ("Task not found"),
    NO_TASK_OWNERSHIP ("Task doesn't belong to the current user");

    private final String defaultMessage;

    ServiceExceptionCode(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getDefaultMessage() {
        return defaultMessage;
    }
}
