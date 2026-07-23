package com.javanauta.ts.taskscheduler.adapters.in.security.enums;

import com.javanauta.ts.taskscheduler.shared.exception.ExceptionCode;

public enum SecurityExceptionCode implements ExceptionCode {
    AUTHENTICATION_ERROR ("Authentication failed");

    private final String defaultMessage;

    SecurityExceptionCode(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getDefaultMessage() {
        return defaultMessage;
    }
}
