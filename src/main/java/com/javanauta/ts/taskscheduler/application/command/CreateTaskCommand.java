package com.javanauta.ts.taskscheduler.application.command;

import lombok.Builder;

import java.time.Instant;
import java.time.ZoneId;

@Builder
public record CreateTaskCommand(
        String name,
        String description,
        Instant scheduledDateTime,
        String userEmail,
        ZoneId timeZoneId) {
}
