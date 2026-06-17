package com.javanauta.ts.taskscheduler.shared.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ApplicationException extends RuntimeException {

    private final ExceptionCode code;
    private final String message;
    private final List<ValidationExceptionDetail> validationExceptionDetails;

    public ApplicationException(ExceptionCode code, String message, List<ValidationExceptionDetail> validationExceptionsDetails) {
        this.code = code;
        this.message = message;
        if (validationExceptionsDetails == null) {
            this.validationExceptionDetails = List.of();
        } else {
            this.validationExceptionDetails = List.copyOf(validationExceptionsDetails);
        }
    }

    public ApplicationException(ExceptionCode code) {
        this(code, code.getDefaultMessage(), null);
    }

    public ApplicationException(ExceptionCode code, List<ValidationExceptionDetail> fieldExceptionsDetails) {
        this(code, code.getDefaultMessage(), fieldExceptionsDetails);
    }

    public ApplicationException(ExceptionCode code, String message) {
        this(code, message, null);
    }
}
