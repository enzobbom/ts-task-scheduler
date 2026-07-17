package com.javanauta.ts.taskscheduler.adapters.in.web.dto.out;

import com.javanauta.ts.taskscheduler.domain.model.enums.NotificationStatus;
import lombok.Builder;

import java.time.Instant;

@Builder
public record TaskResponseDTO(
        String id,
        String name,
        String description,
        Instant creationDateTime,
        Instant scheduledDateTime,
        String userEmail,
        Instant modificationDateTime,
        NotificationStatus notificationStatus,
        String timeZoneId
) {
}
