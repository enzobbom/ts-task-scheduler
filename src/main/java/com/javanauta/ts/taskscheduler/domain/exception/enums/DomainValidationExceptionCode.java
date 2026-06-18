package com.javanauta.ts.taskscheduler.domain.exception.enums;

import com.javanauta.ts.taskscheduler.shared.exception.ValidationExceptionCode;
import com.javanauta.ts.taskscheduler.shared.exception.enums.ValidationExceptionSourceType;

public enum DomainValidationExceptionCode implements ValidationExceptionCode {
    SCHEDULED_DATETIME_IN_THE_PAST(ValidationExceptionSourceType.FIELD, "Scheduled date/time must be in the future.");

    private final ValidationExceptionSourceType exceptionSourceType;
    private final String defaultMessage;

    DomainValidationExceptionCode(ValidationExceptionSourceType exceptionSourceType, String defaultMessage) {
        this.exceptionSourceType = exceptionSourceType;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public ValidationExceptionSourceType getExceptionSourceType() { return exceptionSourceType; }

    @Override
    public String getDefaultMessage() {
        return defaultMessage;
    }
}
