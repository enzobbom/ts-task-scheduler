package com.javanauta.ts.taskscheduler.presentation.exception.enums;

import com.javanauta.ts.taskscheduler.shared.exception.ExceptionCode;

public enum PresentationExceptionCode implements ExceptionCode {
    TYPE_MISMATCH_ERROR("Request parameter type mismatch. One or more fields could not be converted to the expected type"),
    JSON_PARSE_ERROR("Invalid JSON format or invalid data types. The request body could not be parsed"),
    MISSING_PARAMETER_ERROR("Missing required request parameter. One or more required parameters were not provided"),
    INTERNAL_SERVER_ERROR("Internal server error");

    private final String defaultMessage;

    PresentationExceptionCode(String defaultMessage) {
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
