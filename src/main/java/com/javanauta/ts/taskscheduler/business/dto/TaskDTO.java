package com.javanauta.ts.taskscheduler.business.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javanauta.ts.taskscheduler.infrastructure.enums.NotificationStatusEnum;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDTO {

    private String id;
    private String name;
    private String description;
    private Instant creationDateTime;
    private Instant dueDateTime;
    private String userEmail;
    private Instant modificationDateTime;
    private NotificationStatusEnum notificationStatusEnum;
}
