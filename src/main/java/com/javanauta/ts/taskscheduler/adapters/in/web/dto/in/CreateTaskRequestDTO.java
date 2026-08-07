package com.javanauta.ts.taskscheduler.adapters.in.web.dto.in;

import com.javanauta.ts.taskscheduler.adapters.in.web.validation.ValidZoneId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;

@Builder
@Jacksonized
public record CreateTaskRequestDTO(
        @NotBlank String name,
        String description,
        @NotNull Instant scheduledDateTime,
        @NotBlank @ValidZoneId String timeZoneId) {
}
