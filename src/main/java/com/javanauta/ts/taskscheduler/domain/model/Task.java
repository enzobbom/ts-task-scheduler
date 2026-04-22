package com.javanauta.ts.taskscheduler.domain.model;

import com.javanauta.ts.taskscheduler.domain.model.enums.NotificationStatusEnum;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("task")
public class Task {

    @Id
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
