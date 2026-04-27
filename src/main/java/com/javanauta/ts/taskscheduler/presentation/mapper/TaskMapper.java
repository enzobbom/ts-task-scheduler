package com.javanauta.ts.taskscheduler.presentation.mapper;

import com.javanauta.ts.taskscheduler.application.command.CreateTaskCommand;
import com.javanauta.ts.taskscheduler.application.command.UpdateTaskCommand;
import com.javanauta.ts.taskscheduler.domain.model.Task;
import com.javanauta.ts.taskscheduler.presentation.dto.TaskDTO;
import com.javanauta.ts.taskscheduler.presentation.dto.in.CreateTaskRequestDTO;
import com.javanauta.ts.taskscheduler.presentation.dto.in.UpdateTaskRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.ZoneId;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "timeZoneId", expression = "java(TaskMapper.convertTimeZoneId(dto.getTimeZoneId()))")
    CreateTaskCommand fromCreateTaskRequestDTO(CreateTaskRequestDTO createTaskRequestDTO);

    @Mapping(target = "timeZoneId", expression = "java(TaskMapper.convertTimeZoneId(dto.getTimeZoneId()))")
    UpdateTaskCommand fromUpdateTaskRequestDTO(UpdateTaskRequestDTO updateTaskRequestDTO);

    TaskDTO toTaskDTO(Task task);
    List<TaskDTO> toTaskDTOList(List<Task> taskList);

    static ZoneId convertTimeZoneId(String timeZoneIdString) {
        return ZoneId.of(timeZoneIdString);
    }
}
