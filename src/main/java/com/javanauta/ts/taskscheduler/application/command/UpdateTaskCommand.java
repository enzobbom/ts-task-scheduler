package com.javanauta.ts.taskscheduler.application.command;

import com.javanauta.ts.taskscheduler.domain.exception.ValidationErrorException;
import jakarta.validation.Validation;
import lombok.Builder;

import java.time.Instant;
import java.time.ZoneId;

@Builder
public record UpdateTaskCommand(
        String name,
        String description,
        Instant scheduledDateTime,
        ZoneId timeZoneId) {

    public UpdateTaskCommand {
        if (name == null && description == null && scheduledDateTime == null && timeZoneId == null) {
            throw new ValidationErrorException("At least one field must be provided for update.");
        }
    }
}
