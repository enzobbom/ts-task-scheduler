package com.javanauta.ts.taskscheduler.domain.data;

import com.javanauta.ts.taskscheduler.domain.exception.ValidationErrorException;
import lombok.Builder;

import java.time.Instant;
import java.time.ZoneId;

@Builder
public record UpdateTaskData(
        String name,
        String description,
        Instant scheduledDateTime,
        ZoneId timeZoneId) {
}
