package com.javanauta.ts.taskscheduler.domain.exception.enums;

import com.javanauta.ts.taskscheduler.shared.exception.FieldExceptionCode;

public enum DomainFieldExceptionCode implements FieldExceptionCode {
    SCHEDULED_DATETIME_IN_THE_PAST("Scheduled date/time must be in the future.");

    private final String defaultMessage;

    DomainFieldExceptionCode(String defaultMessage) {
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
