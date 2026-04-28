package com.javanauta.ts.taskscheduler.domain.model;

import com.javanauta.ts.taskscheduler.domain.data.CreateTaskData;
import com.javanauta.ts.taskscheduler.domain.data.UpdateTaskData;
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

    public static Task create(CreateTaskData createTaskData, String userEmail) {
        return new Task(
                createTaskData.name(),
                createTaskData.description(),
                createTaskData.scheduledDateTime(),
                userEmail,
                createTaskData.timeZoneId());
    }

    public void updateStatus(NotificationStatusEnum newStatus) {
        if (newStatus == notificationStatusEnum) { return; }
        notificationStatusEnum = newStatus;
        modificationDateTime = Instant.now();
    }

    public void update(UpdateTaskData updateTaskData) {
        if (updateTaskData.name() != null) { name = updateTaskData.name(); }
        if (updateTaskData.description() != null) { description = updateTaskData.description(); }
        if (updateTaskData.scheduledDateTime() != null) {
            scheduledDateTime = updateTaskData.scheduledDateTime();
            validateScheduledDate();
        }
        if (updateTaskData.timeZoneId() != null) { timeZoneId = updateTaskData.timeZoneId(); }
        modificationDateTime = Instant.now();
    }

    private void validateScheduledDate() {
        if (scheduledDateTime.isBefore(Instant.now())) {
            throw new BusinessValidationException("Scheduled date and time must be in the future");
        }
    }
}
