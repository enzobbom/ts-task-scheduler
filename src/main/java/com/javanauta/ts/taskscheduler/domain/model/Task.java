package com.javanauta.ts.taskscheduler.domain.model;

import com.javanauta.ts.taskscheduler.application.command.UpdateTaskCommand;
import com.javanauta.ts.taskscheduler.domain.model.enums.NotificationStatusEnum;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.ZoneId;

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
    private ZoneId timeZoneId;

    private Task(String name, String description, Instant scheduledDateTime, String userEmail, ZoneId timeZoneId) {
        this.name = name;
        this.description = description;
        this.scheduledDateTime = scheduledDateTime;
        this.userEmail = userEmail;
        this.timeZoneId = timeZoneId;

        creationDateTime = Instant.now();
        notificationStatusEnum = NotificationStatusEnum.PENDING;

        validateScheduledDate();
    }

    public static Task create(
            String name,
            String description,
            Instant scheduledDateTime,
            String userEmail,
            ZoneId timeZoneId) {

        return new Task(name, description, scheduledDateTime, userEmail, timeZoneId);
    }

    public void updateStatus(NotificationStatusEnum newStatus) {
        if (newStatus == notificationStatusEnum) { return; }
        notificationStatusEnum = newStatus;
        modificationDateTime = Instant.now();
    }

    public void update(UpdateTaskCommand command) {
        if (command.name() != null) { name = command.name(); }
        if (command.description() != null) { description = command.description(); }
        if (command.scheduledDateTime() != null) {
            scheduledDateTime = command.scheduledDateTime();
            validateScheduledDate();
        }
        if (command.timeZoneId() != null) { timeZoneId = command.timeZoneId(); }
        modificationDateTime = Instant.now();
    }

    private void validateScheduledDate() {
        if (scheduledDateTime.isBefore(Instant.now())) {
            throw new IllegalArgumentException("Scheduled date and time must be in the future");
        }
    }
}
