package com.javanauta.ts.taskscheduler.presentation.response;

public record ValidationErrorDetail(
        String field,
        String message
) {
}