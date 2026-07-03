package com.javanauta.ts.taskscheduler.adapters.in.web.response;

import com.javanauta.ts.taskscheduler.adapters.in.web.response.enums.ResponseStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class Response {
    protected final ResponseStatus status;
    protected final int code;

    public Response(ResponseStatus status, HttpStatus code) {
        this.status = status;
        this.code = code.value();
    }
}
