package com.javanauta.ts.taskscheduler.domain.model;

import com.javanauta.ts.taskscheduler.domain.model.enums.NotificationStatusEnum;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
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

    public Task(String name, String description, Instant scheduledDateTime, String userEmail, String timeZoneId) {
        this.name = name;
        this.description = description;
        this.scheduledDateTime = scheduledDateTime;
        this.userEmail = userEmail;
        this.timeZoneId = timeZoneId;

        initializeForCreation();
    }

    public void update() {
        // ...

        modificationDateTime = Instant.now();
    }

//    public void updateStatus(NotificationStatusEnum newStatus) {
//        if (newStatus == notificationStatusEnum) { return; }
//        notificationStatusEnum = newStatus;
//        modificationDateTime = Instant.now();
//    }

    private void initializeForCreation() {
        creationDateTime = Instant.now();
        notificationStatusEnum = NotificationStatusEnum.PENDING;
    }

    private void validateScheduledDate() {
        if (scheduledDateTime.isBefore(Instant.now())) {
            throw new IllegalArgumentException("Scheduled date and time must be in the future.");
        }
    }
}
