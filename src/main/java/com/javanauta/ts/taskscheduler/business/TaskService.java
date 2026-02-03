package com.javanauta.ts.taskscheduler.business;

import com.javanauta.ts.taskscheduler.business.dto.TaskDTO;
import com.javanauta.ts.taskscheduler.business.mapper.TaskConverter;
import com.javanauta.ts.taskscheduler.business.mapper.TaskUpdateConverter;
import com.javanauta.ts.taskscheduler.infrastructure.entity.Task;
import com.javanauta.ts.taskscheduler.infrastructure.enums.NotificationStatusEnum;
import com.javanauta.ts.taskscheduler.infrastructure.exception.InvalidTimeZoneException;
import com.javanauta.ts.taskscheduler.infrastructure.exception.ResourceNotFoundException;
import com.javanauta.ts.taskscheduler.infrastructure.repository.TaskRepository;
import com.javanauta.ts.taskscheduler.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
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

        Task task = taskConverter.toTask(taskDTO);

        return taskConverter.toTaskDTO(taskRepository.save(task));
    }

    private void validateTimeZoneId(String timeZoneId) {
        if (!ZoneId.getAvailableZoneIds().contains(timeZoneId)) {
            throw new InvalidTimeZoneException("Invalid time zone ID: " + timeZoneId);
        }
    }

    public List<TaskDTO> findTaskByTimePeriod(Instant initialDateTime, Instant finalDateTime) {
        return taskConverter.toTaskDTOList(taskRepository.findByDueDateTimeBetween(initialDateTime, finalDateTime));
    }

    public List<TaskDTO> findTaskByUserEmail(String token) {
        return taskConverter.toTaskDTOList(taskRepository.findByUserEmail(jwtUtil.extractUsername(token.substring(7))));
    }

    public void deleteTaskById(String id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException(String.format("Task with ID '%s' not found.", id));
        }
    }

    public TaskDTO modifyTaskStatusById (NotificationStatusEnum notificationStatusEnum, String id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("Task with ID '%s' not found.", id)));
        task.setNotificationStatusEnum(notificationStatusEnum);

        return taskConverter.toTaskDTO(taskRepository.save(task));
    }

    public TaskDTO updateTaskById (TaskDTO taskDTO, String id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("Task with ID '%s' not found.", id)));

        taskUpdateConverter.updateTasks(taskDTO, task);
        validateTimeZoneId(task.getTimeZoneId());

        return taskConverter.toTaskDTO(taskRepository.save(task));
    }
}
