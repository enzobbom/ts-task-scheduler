package com.javanauta.ts.taskscheduler.presentation.response;

import com.javanauta.ts.taskscheduler.presentation.response.enums.ResponseStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class Response {
    protected final ResponseStatus status;
    protected final HttpStatus httpStatusCode;

    public Response(ResponseStatus status, HttpStatus statusCode) {
        this.status = status;
        this.httpStatusCode = statusCode;
    }
}
