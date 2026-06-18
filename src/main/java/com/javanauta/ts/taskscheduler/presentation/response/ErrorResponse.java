package com.javanauta.ts.taskscheduler.presentation.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.javanauta.ts.taskscheduler.presentation.response.enums.ResponseStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@JsonPropertyOrder({"status", "code", "errorCode", "message", "validationErrors"})
public class ErrorResponse extends Response {

    private final String errorCode;
    private final String message;
    private final List<ValidationErrorDetail> validationErrors;

    public ErrorResponse(HttpStatus code, String errorCode, String message, List<ValidationErrorDetail> validationErrorDetails) {
        super(ResponseStatus.ERROR, code);
        this.errorCode = errorCode;
        this.message = message;
        this.validationErrors = validationErrorDetails;
    }
}
