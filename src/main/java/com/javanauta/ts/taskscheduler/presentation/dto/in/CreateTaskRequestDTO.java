package com.javanauta.ts.taskscheduler.presentation.dto.in;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;

@Builder
@Jacksonized
public record CreateTaskRequestDTO(
        @NotBlank String name,
        @NotNull String description,
        @NotNull Instant scheduledDateTime,
        @NotBlank @Email String userEmail,
        @NotBlank String timeZoneId) {
}
