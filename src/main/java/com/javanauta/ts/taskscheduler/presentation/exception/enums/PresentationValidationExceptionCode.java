package com.javanauta.ts.taskscheduler.presentation.exception.enums;

import com.javanauta.ts.taskscheduler.shared.exception.FieldExceptionCode;

public enum PresentationValidationExceptionCode implements FieldExceptionCode {
    REQUEST_BODY_VALIDATION_ERROR("Invalid request body. One or more fields failed validation"),
    REQUEST_PARAM_VALIDATION_ERROR("Invalid request parameter. One or more fields failed validation");

    private final String defaultMessage;

    PresentationValidationExceptionCode(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getDefaultMessage() {
        return defaultMessage;
    }
}

