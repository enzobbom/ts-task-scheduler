package com.javanauta.ts.taskscheduler.presentation.dto.in;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;

@Builder
@Jacksonized
public record UpdateTaskRequestDTO(
        String name,
        String description,
        Instant scheduledDateTime,
        String timeZoneId) {
}
