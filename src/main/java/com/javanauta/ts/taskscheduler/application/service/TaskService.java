package com.javanauta.ts.taskscheduler.application.service;

import com.javanauta.ts.taskscheduler.application.exception.ForbiddenException;
import com.javanauta.ts.taskscheduler.application.exception.ResourceNotFoundException;
import com.javanauta.ts.taskscheduler.application.exception.ServiceValidationException;
import com.javanauta.ts.taskscheduler.application.ports.CurrentUserProvider;
import com.javanauta.ts.taskscheduler.domain.data.CreateTaskData;
import com.javanauta.ts.taskscheduler.domain.data.UpdateTaskData;
import com.javanauta.ts.taskscheduler.domain.model.Task;
import com.javanauta.ts.taskscheduler.domain.model.enums.NotificationStatusEnum;
import com.javanauta.ts.taskscheduler.infrastructure.repository.TaskRepository;
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

    private final TaskRepository taskRepository;
    private final CurrentUserProvider currentUserProvider;

    public Task createTask(CreateTaskData createTaskData) {
        String userEmail = currentUserProvider.getEmail();
        Task task = Task.create(createTaskData, userEmail);

        Task savedTask = taskRepository.save(task);
        log.info("Task {} created", savedTask.getId());

        return savedTask;
    }

    public List<Task> findTasksByTimePeriod(Instant initialDateTime, Instant finalDateTime) {
        // End point to use internally. Will be refactored to be authenticated using M2M
        validateTimePeriod(initialDateTime, finalDateTime);
        return taskRepository.findByScheduledDateTimeBetween(initialDateTime, finalDateTime);
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
    public Task updateTaskStatus(NotificationStatusEnum notificationStatusEnum, String id) {
        Task task = getTaskOrThrow(id);
        validateTaskOwnership(task);
        task.updateStatus(notificationStatusEnum);

        log.info("Status of task {} updated", id);
        return task;
    }

    @Transactional
    public Task updateTask(UpdateTaskData updateTaskData, String id) {
        Task task = getTaskOrThrow(id);
        validateTaskOwnership(task);
        task.update(updateTaskData);

        log.info("Task {} updated", id);
        return task;
    }

    // internal helper/validation methods

    private Task getTaskOrThrow(String id) {
        return taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    private void validateTaskOwnership(Task task) {
        if (!task.getUserEmail().equals(currentUserProvider.getEmail())) {
            throw new ForbiddenException("User not authenticated to perform this action");
        }
    }

    private void validateTimePeriod(Instant initialDateTime, Instant finalDateTime) {
        if (!initialDateTime.isBefore(finalDateTime)) {
            throw new ServiceValidationException("Initial date time must be before final date time");
        }
    }
}
