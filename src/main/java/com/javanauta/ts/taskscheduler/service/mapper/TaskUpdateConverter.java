package com.javanauta.ts.taskscheduler.service.mapper;

import com.javanauta.ts.taskscheduler.service.dto.TaskDTO;
import com.javanauta.ts.taskscheduler.infrastructure.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskUpdateConverter {

    void updateTasks(TaskDTO taskDTO, @MappingTarget Task task);
}
