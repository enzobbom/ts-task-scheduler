package com.javanauta.taskscheduler.business;

import com.javanauta.taskscheduler.business.dto.TaskDTO;
import com.javanauta.taskscheduler.business.dto.UserDTO;
import com.javanauta.taskscheduler.business.mapper.TaskConverter;
import com.javanauta.taskscheduler.infrastructure.client.UserClient;
import com.javanauta.taskscheduler.infrastructure.entity.Task;
import com.javanauta.taskscheduler.infrastructure.enums.NotificationStatusEnum;
import com.javanauta.taskscheduler.infrastructure.repository.TaskRepository;
import com.javanauta.taskscheduler.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskConverter taskConverter;
    private final JwtUtil jwtUtil;

    public TaskDTO saveTask(String token, TaskDTO taskDTO) {

        taskDTO.setUserEmail(jwtUtil.extractUsername(token.substring(7)));
        taskDTO.setCreationDateTime(LocalDateTime.now());
        taskDTO.setNotificationStatusEnum(NotificationStatusEnum.PENDING);

        Task task = taskConverter.toTask(taskDTO);

        return taskConverter.toTaskDTO(taskRepository.save(task));
    }

    public List<TaskDTO> findTaskByTimePeriod(LocalDateTime initialDateTime, LocalDateTime finalDateTime) {
        return taskConverter.toTaskDTOList(taskRepository.findByDueDateTimeBetween(initialDateTime, finalDateTime));
    }
}
