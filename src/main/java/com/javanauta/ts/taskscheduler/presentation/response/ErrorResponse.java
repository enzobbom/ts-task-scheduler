package com.javanauta.ts.taskscheduler.presentation.response;

import com.javanauta.ts.taskscheduler.presentation.response.enums.ResponseStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class ErrorResponse extends Response {

    private final String errorCode;
    private final String message;
    private final List<FieldErrorDetail> fieldErrorDetails;

    public ErrorResponse(HttpStatus statusCode, String errorCode, String message, List<FieldErrorDetail> fieldErrorDetails) {
        super(ResponseStatus.ERROR, statusCode);
        this.errorCode = errorCode;
        this.message = message;
        this.fieldErrorDetails = fieldErrorDetails;
    }
}
