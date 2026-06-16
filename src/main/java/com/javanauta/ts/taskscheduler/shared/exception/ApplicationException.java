package com.javanauta.ts.taskscheduler.shared.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ApplicationException extends RuntimeException {

    private final ExceptionCode code;
    private final String message;
    private final List<FieldExceptionDetail> fieldExceptionDetails;

    public ApplicationException(ExceptionCode code, String message, List<FieldExceptionDetail> fieldExceptionsDetails) {
        this.code = code;
        this.message = message;
        this.fieldExceptionDetails = List.copyOf(fieldExceptionsDetails);
    }

    public ApplicationException(ExceptionCode code) {
        this(code, code.getDefaultMessage(), null);
    }}
}
