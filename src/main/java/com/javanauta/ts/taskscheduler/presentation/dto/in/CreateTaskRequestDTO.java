package com.javanauta.ts.taskscheduler.presentation.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;

@Builder
@Jacksonized
public record CreateTaskRequestDTO(
        @NotBlank String name,
        @NotNull String description,
        @NotNull Instant scheduledDateTime,
        @NotBlank String timeZoneId) {
}
