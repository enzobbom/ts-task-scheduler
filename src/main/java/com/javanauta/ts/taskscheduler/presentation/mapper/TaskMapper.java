package com.javanauta.ts.taskscheduler.presentation.mapper;

import com.javanauta.ts.taskscheduler.domain.data.TaskData;
import com.javanauta.ts.taskscheduler.domain.model.Task;
import com.javanauta.ts.taskscheduler.presentation.dto.out.TaskResponseDTO;
import com.javanauta.ts.taskscheduler.presentation.dto.in.CreateTaskRequestDTO;
import com.javanauta.ts.taskscheduler.presentation.dto.in.UpdateTaskRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.ZoneId;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, imports = { java.time.ZoneId.class })
public interface TaskMapper {

    @Mapping(target = "timeZoneId", expression = "java(ZoneId.of(createTaskRequestDTO.timeZoneId()))")
    TaskData fromCreateTaskRequestDTO(CreateTaskRequestDTO createTaskRequestDTO);

    @Mapping(target = "timeZoneId", expression = "java(updateTaskRequestDTO.timeZoneId() != null ? ZoneId.of(updateTaskRequestDTO.timeZoneId()) : null)")
    TaskData fromUpdateTaskRequestDTO(UpdateTaskRequestDTO updateTaskRequestDTO);

    @Mapping(target = "timeZoneId", expression = "java(task.getTimeZoneId().getId())")
    TaskResponseDTO toTaskDTO(Task task);

    List<TaskResponseDTO> toTaskDTOList(List<Task> taskList);

    static ZoneId convertStringToTimeZoneId(String timeZoneIdString) {
        return ZoneId.of(timeZoneIdString);
    }

    static String convertTimeZoneIdToString(ZoneId timeZoneId) {
        return timeZoneId.getId();
    }
}
