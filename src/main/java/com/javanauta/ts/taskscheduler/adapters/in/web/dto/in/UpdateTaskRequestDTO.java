package com.javanauta.ts.taskscheduler.adapters.in.web.dto.in;

import com.javanauta.ts.taskscheduler.adapters.in.web.validation.AtLeastOneField;
import com.javanauta.ts.taskscheduler.adapters.in.web.validation.ValidZoneId;
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
