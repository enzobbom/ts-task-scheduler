package com.javanauta.ts.taskscheduler.domain.model;

import com.javanauta.ts.taskscheduler.domain.data.TaskData;
import com.javanauta.ts.taskscheduler.domain.exception.BusinessValidationException;
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

    public static Task create(TaskData taskData, String userEmail) {
        return new Task(
                taskData.name(),
                taskData.description(),
                taskData.scheduledDateTime(),
                userEmail,
                taskData.timeZoneId());
    }

    public void updateStatus(NotificationStatusEnum newStatus) {
        if (newStatus == notificationStatusEnum) { return; }
        notificationStatusEnum = newStatus;
        modificationDateTime = Instant.now();
    }

    public void update(TaskData taskData) {
        if (taskData.name() != null) { name = taskData.name(); }
        if (taskData.description() != null) { description = taskData.description(); }
        if (taskData.scheduledDateTime() != null) {
            scheduledDateTime = taskData.scheduledDateTime();
            validateScheduledDate();
        }
        if (taskData.timeZoneId() != null) { timeZoneId = taskData.timeZoneId(); }
        modificationDateTime = Instant.now();
    }

    private void validateScheduledDate() {
        if (scheduledDateTime.isBefore(Instant.now())) {
            throw new BusinessValidationException("Scheduled date and time must be in the future");
        }
    }
}
