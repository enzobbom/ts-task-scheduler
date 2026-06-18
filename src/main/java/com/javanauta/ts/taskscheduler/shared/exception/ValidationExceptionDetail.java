package com.javanauta.ts.taskscheduler.shared.exception;

import com.javanauta.ts.taskscheduler.shared.exception.enums.ValidationExceptionSourceType;

public record ValidationExceptionDetail(ValidationExceptionCode code, String source, String message) {

    public ValidationExceptionDetail(ValidationExceptionCode code, String source) {
        this(code, source, code.getDefaultMessage());
    }
}
