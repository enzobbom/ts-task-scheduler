package com.javanauta.ts.taskscheduler.domain.data;

import lombok.Builder;

import java.time.Instant;
import java.time.ZoneId;

@Builder
public record CreateTaskData(
        String name,
        String description,
        Instant scheduledDateTime,
        String userEmail,
        ZoneId timeZoneId) {
}
