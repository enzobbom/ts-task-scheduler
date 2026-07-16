package com.javanauta.ts.taskscheduler.application.service;

import com.javanauta.ts.taskscheduler.application.data.NotificationResultDetails;
import com.javanauta.ts.taskscheduler.application.data.enums.NotificationResult;
import com.javanauta.ts.taskscheduler.application.exception.enums.ServiceExceptionCode;
import com.javanauta.ts.taskscheduler.domain.data.TaskData;
import com.javanauta.ts.taskscheduler.domain.model.Task;
import com.javanauta.ts.taskscheduler.domain.model.enums.NotificationStatus;
import com.javanauta.ts.taskscheduler.ports.out.messaging.NotificationRequestPublisher;
import com.javanauta.ts.taskscheduler.ports.out.persistence.TaskPersister;
import com.javanauta.ts.taskscheduler.ports.out.security.PrincipalProvider;
import com.javanauta.ts.taskscheduler.shared.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {
    private final TaskPersister taskPersister;
    private final PrincipalProvider principalProvider;
    private final NotificationRequestPublisher notificationRequestPublisher;

    // Controller

    public Task createTask(TaskData taskData) {
        String userEmail = principalProvider.getEmail();
        Task task = Task.create(taskData, userEmail);

        Task savedTask = taskPersister.save(task);
        log.info("Task {} created", savedTask.getId());

        return savedTask;
    }

    public List<Task> findTasksByUserEmail() {
        return taskPersister.findByUserEmail(principalProvider.getEmail());
    }

    public void deleteTask(String id) {
        Task task = getTaskOrThrow(id);
        validateTaskOwnership(task);
        taskPersister.deleteById(id);

        log.info("Task {} deleted", id);
    }

    @Transactional
    public Task updateTask(TaskData taskData, String id) {
        Task task = getTaskOrThrow(id);
        validateTaskOwnership(task);
        task.update(taskData);

        log.info("Task {} updated", id);
        return task;
    }

    // Scheduler

    public List<Task> findTasksToNotify(Instant initialDateTime, Instant finalDateTime) {
        return taskPersister.findByNotificationStatusInAndScheduledDateTimeBetween(
                NotificationStatus.notifiableStatuses(),
                initialDateTime,
                finalDateTime);
    }

    public void requestTaskNotification(Task task) {
        if (!task.canBeNotified()) { return; }

        notificationRequestPublisher.publishNotificationRequest(task);
        updateTaskStatus(task, NotificationStatus.DISPATCHED);

        log.info("Notification request for Task '{}' was successfully dispatched", task.getId());
    }

    // Messaging Broker

    public void processTaskNotificationCompletion(NotificationResultDetails resultDetails) {
        String taskId = resultDetails.taskId();
        Task task = getTaskOrThrow(taskId);
        updateTaskStatus(task, NotificationStatus.NOTIFIED);

        log.info("Notification for Task '{}' was successfully completed", taskId);
    }

    public void processTaskNotificationFailure(NotificationResultDetails resultDetails) {
        NotificationResult notificationResult = resultDetails.notificationResult();
        String taskId = resultDetails.taskId();

        log.info("Notification for Task '{}' failed ({})", taskId, notificationResult.name().toLowerCase());
        Task task = getTaskOrThrow(taskId);

        NotificationStatus newStatus = switch (notificationResult) {
            case TEMPORARY_FAILURE -> NotificationStatus.PENDING_RETRY;
            case PERMANENT_FAILURE -> NotificationStatus.FAILED;
            default -> throw new IllegalArgumentException("Only 'FAILURE' results are expected here");
        };

        updateTaskStatus(task, newStatus);
    }

    // internal helper/validation methods

    private Task getTaskOrThrow(String id) {
        return taskPersister.findById(id).orElseThrow(()
                -> new ApplicationException(ServiceExceptionCode.TASK_NOT_FOUND));
    }

    private void validateTaskOwnership(Task task) {
        if (!task.getUserEmail().equals(principalProvider.getEmail())) {
            throw new ApplicationException(ServiceExceptionCode.NO_TASK_OWNERSHIP);
        }
    }

    private void updateTaskStatus(Task task, NotificationStatus status) {
        task.updateStatus(status);
        taskPersister.save(task);

        log.info("Status of task {} updated to {}", task.getId(), status);
    }
}
