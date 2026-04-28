package com.javanauta.ts.taskscheduler.domain.data;

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
