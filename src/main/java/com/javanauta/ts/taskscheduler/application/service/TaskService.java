package com.javanauta.ts.taskscheduler.application.service;

import com.javanauta.ts.taskscheduler.application.command.CreateTaskCommand;
import com.javanauta.ts.taskscheduler.presentation.dto.TaskDTO;
import com.javanauta.ts.taskscheduler.application.mapper.TaskConverter;
import com.javanauta.ts.taskscheduler.application.mapper.TaskUpdateConverter;
import com.javanauta.ts.taskscheduler.domain.model.Task;
import com.javanauta.ts.taskscheduler.domain.model.enums.NotificationStatusEnum;
import com.javanauta.ts.taskscheduler.domain.exception.ValidationErrorException;
import com.javanauta.ts.taskscheduler.domain.exception.ResourceNotFoundException;
import com.javanauta.ts.taskscheduler.infrastructure.repository.TaskRepository;
import com.javanauta.ts.taskscheduler.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskConverter taskConverter;
    private final JwtUtil jwtUtil;
    private final TaskUpdateConverter taskUpdateConverter;

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

    public List<Task> findTaskByTimePeriod(Instant initialDateTime, Instant finalDateTime) {
        validateTimePeriod(initialDateTime, finalDateTime);
        return taskRepository.findByScheduledDateTimeBetween(initialDateTime, finalDateTime);
    }

    public List<TaskDTO> findTaskByUserEmail(String token) {
        // Assumes the user exists as it's being extracted from the token
        return taskConverter.toTaskDTOList(taskRepository.findByUserEmail(jwtUtil.extractUsername(token.substring(7))));
    }

    public void deleteTask(String id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            log.info("Task {} deleted", id);
        } else {
            throw new ResourceNotFoundException("Task not found");
        }
    }

    public TaskDTO updateTaskStatus(NotificationStatusEnum notificationStatusEnum, String id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        task.setNotificationStatusEnum(notificationStatusEnum);
        task.setModificationDateTime(Instant.now());

        Task updatedTask = taskRepository.save(task);
        log.info("Status of task {} updated", updatedTask.getId());

        return taskConverter.toTaskDTO(updatedTask);
    }

    public TaskDTO updateTask(TaskDTO taskDTO, String id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        taskUpdateConverter.updateTasks(taskDTO, task);
        task.setModificationDateTime(Instant.now());

        Task updatedTask = taskRepository.save(task);
        log.info("Task {} updated", updatedTask.getId());

        return taskConverter.toTaskDTO(updatedTask);
    }

    // internal validation methods

    private void validateTimePeriod(Instant initialDateTime, Instant finalDateTime) {
        if (!initialDateTime.isBefore(finalDateTime)) {
            throw new ValidationErrorException("Initial date time must be before final date time");
        }
    }
}
