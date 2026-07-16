package com.javanauta.ts.taskscheduler.domain.model;

import com.javanauta.ts.taskscheduler.domain.data.TaskData;
import com.javanauta.ts.taskscheduler.domain.exception.enums.DomainExceptionCode;
import com.javanauta.ts.taskscheduler.domain.exception.enums.DomainValidationExceptionCode;
import com.javanauta.ts.taskscheduler.domain.model.enums.NotificationStatus;
import com.javanauta.ts.taskscheduler.shared.exception.ApplicationException;
import com.javanauta.ts.taskscheduler.shared.exception.ValidationExceptionDetail;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("task")
@CompoundIndex(
        name = "notification_schedule_idx",
        def = "{'notificationStatus': 1, 'scheduledDateTime': 1}"
)
public class Task {
    @Id
    private String id;
    private String name;
    private String description;
    private Instant creationDateTime;
    private Instant scheduledDateTime;
    @Indexed
    private String userEmail;
    private Instant modificationDateTime;
    private NotificationStatus notificationStatus;
    private ZoneId timeZoneId;

    private final static String SCHEDULED_DATETIME_FIELD_NAME = "scheduledDateTime";

    private Task(String name, String description, Instant scheduledDateTime, String userEmail, ZoneId timeZoneId) {
        this.name = name;
        this.description = description;
        this.scheduledDateTime = scheduledDateTime;
        this.userEmail = userEmail;
        this.timeZoneId = timeZoneId;

        creationDateTime = Instant.now();
        notificationStatus = NotificationStatus.PENDING;

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

    public void updateStatus(NotificationStatus newStatus) {
        if (newStatus == notificationStatus) { return; }
        notificationStatus = newStatus;
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

    public boolean canBeNotified() {
        return NotificationStatus.notifiableStatuses().contains(notificationStatus);
    }

    private void validateScheduledDate() {
        if (scheduledDateTime.isBefore(Instant.now())) {

            ValidationExceptionDetail detail = new ValidationExceptionDetail(
                    DomainValidationExceptionCode.SCHEDULED_DATETIME_IN_THE_PAST, SCHEDULED_DATETIME_FIELD_NAME);

            throw new ApplicationException(
                    DomainExceptionCode.DOMAIN_VALIDATION_ERROR, List.of(detail));
        }
    }
}
