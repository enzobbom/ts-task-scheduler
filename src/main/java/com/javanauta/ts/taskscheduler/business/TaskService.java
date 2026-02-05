package com.javanauta.ts.taskscheduler.business;

import com.javanauta.ts.taskscheduler.business.dto.TaskDTO;
import com.javanauta.ts.taskscheduler.business.mapper.TaskConverter;
import com.javanauta.ts.taskscheduler.business.mapper.TaskUpdateConverter;
import com.javanauta.ts.taskscheduler.infrastructure.entity.Task;
import com.javanauta.ts.taskscheduler.infrastructure.enums.NotificationStatusEnum;
import com.javanauta.ts.taskscheduler.infrastructure.exception.ValidationErrorException;
import com.javanauta.ts.taskscheduler.infrastructure.exception.ResourceNotFoundException;
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

    public TaskDTO saveTask(String token, TaskDTO taskDTO) {
        taskDTO.setUserEmail(jwtUtil.extractUsername(token.substring(7)));
        taskDTO.setCreationDateTime(Instant.now());
        taskDTO.setNotificationStatusEnum(NotificationStatusEnum.PENDING);
        validateTimeZoneId(taskDTO.getTimeZoneId());

        Task savedTask = taskRepository.save(taskConverter.toTask(taskDTO));
        log.info("Task {} created", savedTask.getId());

        return taskConverter.toTaskDTO(savedTask);
    }

    private void validateTimeZoneId(String timeZoneId) {
        if (!ZoneId.getAvailableZoneIds().contains(timeZoneId)) {
            throw new ValidationErrorException("Invalid time zone ID");
        }
    }

    public List<TaskDTO> findTaskByTimePeriod(Instant initialDateTime, Instant finalDateTime) {
        return taskConverter.toTaskDTOList(taskRepository.findByScheduledDateTimeBetween(initialDateTime, finalDateTime));
    }

    public List<TaskDTO> findTaskByUserEmail(String token) {
        // Assumes the user exists as it's being extracted from the token
        return taskConverter.toTaskDTOList(taskRepository.findByUserEmail(jwtUtil.extractUsername(token.substring(7))));
    }

    public void deleteTaskById(String id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            log.info("Task {} deleted", id);
        } else {
            throw new ResourceNotFoundException("Task not found");
        }
    }

    public TaskDTO modifyTaskStatusById (NotificationStatusEnum notificationStatusEnum, String id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        task.setNotificationStatusEnum(notificationStatusEnum);
        task.setModificationDateTime(Instant.now());

        Task updatedTask = taskRepository.save(task);
        log.info("Status of task {} updated", updatedTask.getId());

        return taskConverter.toTaskDTO(updatedTask);
    }

    public TaskDTO updateTaskById (TaskDTO taskDTO, String id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        taskUpdateConverter.updateTasks(taskDTO, task);
        validateTimeZoneId(task.getTimeZoneId());
        task.setModificationDateTime(Instant.now());

        Task updatedTask = taskRepository.save(task);
        log.info("Task {} updated", updatedTask.getId());

        return taskConverter.toTaskDTO(updatedTask);
    }
}
