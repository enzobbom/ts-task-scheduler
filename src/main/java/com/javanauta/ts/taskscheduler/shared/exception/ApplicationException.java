package com.javanauta.ts.taskscheduler.shared.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ApplicationException extends RuntimeException {

    private final List<ExceptionDetail> exceptionDetails;

    public ApplicationException(List<ExceptionDetail> exceptionDetails) {

        this.exceptionDetails = List.copyOf(exceptionDetails);
    }
}
