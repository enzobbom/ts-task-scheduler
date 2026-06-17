package com.javanauta.ts.taskscheduler.domain.exception.enums;

import com.javanauta.ts.taskscheduler.shared.exception.ValidationExceptionCode;

public enum DomainValidationExceptionCode implements ValidationExceptionCode {
    SCHEDULED_DATETIME_IN_THE_PAST("Scheduled date/time must be in the future.");

    private final String defaultMessage;

    DomainValidationExceptionCode(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getDefaultMessage() {
        return defaultMessage;
    }
}
