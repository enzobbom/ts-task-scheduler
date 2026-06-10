package com.javanauta.ts.taskscheduler.presentation.dto.out;

import com.javanauta.ts.taskscheduler.domain.model.enums.NotificationStatusEnum;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskResponseDTO {

    private String id;
    private String name;
    private String description;
    private Instant creationDateTime;
    private Instant scheduledDateTime;
    private String userEmail;
    private Instant modificationDateTime;
    private NotificationStatusEnum notificationStatusEnum;
    private String timeZoneId;
}
