package com.javanauta.ts.taskscheduler.domain.exception.enums;

import com.javanauta.ts.taskscheduler.shared.exception.ExceptionCode;

public enum DomainExceptionCode implements ExceptionCode {
    SCHEDULED_TIME_IN_THE_PAST ("Scheduled date/time must be in the future.");

    private final String defaultMessage;

    DomainExceptionCode(String defaultMessage) {
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
