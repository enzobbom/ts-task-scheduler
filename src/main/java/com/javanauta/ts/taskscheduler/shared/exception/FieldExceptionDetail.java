package com.javanauta.ts.taskscheduler.shared.exception;

public record FieldExceptionDetail(FieldExceptionCode code, String field, String message) {

    public FieldExceptionDetail(FieldExceptionCode code, String field) {
        this(code, field, code.getDefaultMessage());
    }
}
