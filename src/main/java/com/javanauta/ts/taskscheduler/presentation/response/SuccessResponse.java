package com.javanauta.ts.taskscheduler.presentation.response;

import com.javanauta.ts.taskscheduler.presentation.response.enums.ResponseStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SuccessResponse<T> extends Response {

    private final T data;

    public SuccessResponse(HttpStatus statusCode, T data) {
        super(ResponseStatus.SUCCESS, statusCode);
        this.data = data;
    }
}
