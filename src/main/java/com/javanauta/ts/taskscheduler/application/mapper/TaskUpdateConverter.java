package com.javanauta.ts.taskscheduler.application.mapper;

import com.javanauta.ts.taskscheduler.presentation.dto.TaskDTO;
import com.javanauta.ts.taskscheduler.domain.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskUpdateConverter {

    void updateTasks(TaskDTO taskDTO, @MappingTarget Task task);
}
