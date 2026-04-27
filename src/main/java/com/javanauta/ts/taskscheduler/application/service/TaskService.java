package com.javanauta.ts.taskscheduler.application.service;

import com.javanauta.ts.taskscheduler.application.command.CreateTaskCommand;
import com.javanauta.ts.taskscheduler.application.command.UpdateTaskCommand;
import com.javanauta.ts.taskscheduler.application.mapper.TaskConverter;
import com.javanauta.ts.taskscheduler.application.mapper.TaskUpdateConverter;
import com.javanauta.ts.taskscheduler.domain.exception.ResourceNotFoundException;
import com.javanauta.ts.taskscheduler.domain.exception.ValidationErrorException;
import com.javanauta.ts.taskscheduler.domain.model.Task;
import com.javanauta.ts.taskscheduler.domain.model.enums.NotificationStatusEnum;
import com.javanauta.ts.taskscheduler.infrastructure.repository.TaskRepository;
import com.javanauta.ts.taskscheduler.infrastructure.security.JwtUtil;
import com.javanauta.ts.taskscheduler.presentation.dto.TaskDTO;
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
    private final TaskConverter taskConverter;
    private final JwtUtil jwtUtil;
    private final TaskUpdateConverter taskUpdateConverter;

    private static final String TASK_NOT_FOUND_MSG = "Task not found";

    public Task createTask(String token, CreateTaskCommand createTaskCommand) {
        String userEmail = jwtUtil.extractUsername(token.substring(7));

        Task task = Task.create(
                createTaskCommand.name(),
                createTaskCommand.description(),
                createTaskCommand.scheduledDateTime(),
                userEmail,
                createTaskCommand.timeZoneId());

        Task savedTask = taskRepository.save(task);
        log.info("Task {} created", savedTask.getId());

        return savedTask;
    }

    public List<Task> findTasksByTimePeriod(Instant initialDateTime, Instant finalDateTime) {
        validateTimePeriod(initialDateTime, finalDateTime);
        return taskRepository.findByScheduledDateTimeBetween(initialDateTime, finalDateTime);
    }

    public List<Task> findTasksByUserEmail(String token) {
        String userEmail = jwtUtil.extractUsername(token.substring(7));
        return taskRepository.findByUserEmail(userEmail);
    }

    public void deleteTask(String id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            log.info("Task {} deleted", id);
        } else {
            throw new ResourceNotFoundException(TASK_NOT_FOUND_MSG);
        }
    }

    @Transactional
    public Task updateTaskStatus(NotificationStatusEnum notificationStatusEnum, String id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(TASK_NOT_FOUND_MSG));
        task.updateStatus(notificationStatusEnum);

        log.info("Status of task {} updated", id);
        return task;
    }

    @Transactional
    public Task updateTask(UpdateTaskCommand command, String id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(TASK_NOT_FOUND_MSG));
        task.update(command);

        log.info("Task {} updated", id);
        return task;
    }

    // internal validation methods

    private void validateTimePeriod(Instant initialDateTime, Instant finalDateTime) {
        if (!initialDateTime.isBefore(finalDateTime)) {
            throw new ValidationErrorException("Initial date time must be before final date time");
        }
    }
}
