package com.javanauta.ts.taskscheduler.presentation.dto.in;

import com.javanauta.ts.taskscheduler.presentation.validation.AtLeastOneField;
import com.javanauta.ts.taskscheduler.presentation.validation.ValidZoneId;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;

@Builder
@Jacksonized
@AtLeastOneField
public record UpdateTaskRequestDTO(
        String name,
        String description,
        Instant scheduledDateTime,
        @ValidZoneId String timeZoneId) {
}
