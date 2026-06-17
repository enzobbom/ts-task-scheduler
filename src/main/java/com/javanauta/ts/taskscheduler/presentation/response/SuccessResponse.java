package com.javanauta.ts.taskscheduler.presentation.response;

import com.javanauta.ts.taskscheduler.presentation.response.enums.ResponseStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SuccessResponse<T> extends Response {

    private final T data;

    public SuccessResponse(HttpStatus code, T data) {
        super(ResponseStatus.SUCCESS, code);
        this.data = data;
    }

    public SuccessResponse(HttpStatus code) {
        super(ResponseStatus.SUCCESS, code);
        this.data = null;
    }
}
