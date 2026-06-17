package com.javanauta.ts.taskscheduler.presentation.response;

import com.javanauta.ts.taskscheduler.presentation.response.enums.ResponseStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class ErrorResponse extends Response {

    private final String errorCode;
    private final String message;
    private final List<FieldErrorDetail> fieldErrors;

    public ErrorResponse(HttpStatus code, String errorCode, String message, List<FieldErrorDetail> fieldErrorDetails) {
        super(ResponseStatus.ERROR, code);
        this.errorCode = errorCode;
        this.message = message;
        this.fieldErrors = fieldErrorDetails;
    }
}
