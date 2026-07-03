package com.javanauta.ts.taskscheduler.application.service;

import com.javanauta.ts.events.notification.NotificationCompletedEvent;
import com.javanauta.ts.events.notification.NotificationRequestEvent;
import com.javanauta.ts.taskscheduler.application.exception.enums.ServiceExceptionCode;
import com.javanauta.ts.taskscheduler.application.ports.CurrentUserProvider;
import com.javanauta.ts.taskscheduler.application.ports.NotificationRequestPublisher;
import com.javanauta.ts.taskscheduler.domain.data.TaskData;
import com.javanauta.ts.taskscheduler.domain.model.Task;
import com.javanauta.ts.taskscheduler.domain.model.enums.NotificationStatus;
import com.javanauta.ts.taskscheduler.infrastructure.repository.TaskRepository;
import com.javanauta.ts.taskscheduler.shared.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;
    private final CurrentUserProvider currentUserProvider;
    private final NotificationRequestPublisher notificationRequestPublisher;

    // Controller

    public Task createTask(TaskData taskData) {
        String userEmail = currentUserProvider.getEmail();
        Task task = Task.create(taskData, userEmail);

        Task savedTask = taskRepository.save(task);
        log.info("Task {} created", savedTask.getId());

        return savedTask;
    }

    public List<Task> findTasksByUserEmail() {
        return taskRepository.findByUserEmail(currentUserProvider.getEmail());
    }

    public void deleteTask(String id) {
        Task task = getTaskOrThrow(id);
        validateTaskOwnership(task);
        taskRepository.deleteById(id);

        log.info("Task {} deleted", id);
    }

    @Transactional
    public Task updateTaskStatus(NotificationStatus notificationStatus, String id) {
        Task task = getTaskOrThrow(id);
        validateTaskOwnership(task);
        task.updateStatus(notificationStatus);

        log.info("Status of task {} updated", id);
        return task;
    }

    @Transactional
    public Task updateTask(TaskData taskData, String id) {
        Task task = getTaskOrThrow(id);
        validateTaskOwnership(task);
        task.update(taskData);

        log.info("Task {} updated", id);
        return task;
    }

    // Cron

    public List<Task> findTasksByTimePeriod(Instant initialDateTime, Instant finalDateTime) {
        return taskRepository.findByScheduledDateTimeBetween(initialDateTime, finalDateTime);
    }

    public void requestTaskNotification(Task task) {
        if (!task.canBeNotified()) {return;}

        String taskId = task.getId();

        NotificationRequestEvent event = new NotificationRequestEvent(
                UUID.randomUUID(),
                Instant.now(),
                taskId,
                task.getName(),
                task.getDescription(),
                task.getScheduledDateTime(),
                task.getUserEmail(),
                task.getTimeZoneId().toString());

        notificationRequestPublisher.publishNotificationRequest(event);

        task.updateStatus(NotificationStatus.DISPATCHED);
        taskRepository.save(task);
    }

    // Messaging Broker

    public void processTaskNotificationCompletion(NotificationCompletedEvent event) {
        Task task = getTaskOrThrow(event.taskId());

        task.updateStatus(NotificationStatus.NOTIFIED);
        taskRepository.save(task);
    }

    // internal helper/validation methods

    private Task getTaskOrThrow(String id) {
        return taskRepository.findById(id).orElseThrow(()
                -> new ApplicationException(ServiceExceptionCode.TASK_NOT_FOUND));
    }

    private void validateTaskOwnership(Task task) {
        if (!task.getUserEmail().equals(currentUserProvider.getEmail())) {
            throw new ApplicationException(ServiceExceptionCode.NO_TASK_OWNERSHIP);
        }
    }
}
