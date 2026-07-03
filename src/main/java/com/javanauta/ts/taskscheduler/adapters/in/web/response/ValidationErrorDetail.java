package com.javanauta.ts.taskscheduler.adapters.in.web.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"sourceType", "source", "message"})
public record ValidationErrorDetail(
        String sourceType,
        String source,
        String message
) {
}