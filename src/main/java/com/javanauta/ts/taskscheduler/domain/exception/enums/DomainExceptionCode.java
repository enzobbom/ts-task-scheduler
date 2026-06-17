package com.javanauta.ts.taskscheduler.domain.exception.enums;

import com.javanauta.ts.taskscheduler.shared.exception.ExceptionCode;

public enum DomainExceptionCode implements ExceptionCode {
    DOMAIN_VALIDATION_ERROR ("One or more fields failed domain business validation");

    private final String defaultMessage;

    DomainExceptionCode(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getIdentifier() {
        return name();
    }

    @Override
    public String getDefaultMessage() {
        return defaultMessage;
    }
}
