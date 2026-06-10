package com.javanauta.ts.taskscheduler.presentation.controller;

import com.javanauta.ts.taskscheduler.application.service.TaskService;
import com.javanauta.ts.taskscheduler.domain.model.Task;
import com.javanauta.ts.taskscheduler.domain.model.enums.NotificationStatusEnum;
import com.javanauta.ts.taskscheduler.presentation.dto.TaskDTO;
import com.javanauta.ts.taskscheduler.presentation.dto.in.CreateTaskRequestDTO;
import com.javanauta.ts.taskscheduler.presentation.dto.in.UpdateTaskRequestDTO;
import com.javanauta.ts.taskscheduler.presentation.mapper.TaskMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@Validated
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(
            @RequestBody @Valid CreateTaskRequestDTO createTaskRequestDTO) {

        Task createdTask = taskService.createTask(taskMapper.fromCreateTaskRequestDTO(createTaskRequestDTO));
        return ResponseEntity.ok(taskMapper.toTaskDTO(createdTask));
    }

    // To be removed (and used internally only)
    @GetMapping("/events")
    public ResponseEntity<List<TaskDTO>> findTasksByPeriod(
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant initialDateTime,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant finalDateTime) {

        List<Task> tasks = taskService.findTasksByTimePeriod(initialDateTime, finalDateTime);
        return ResponseEntity.ok(taskMapper.toTaskDTOList(tasks));
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> findTasksByUserEmail() {

        List<Task> tasks = taskService.findTasksByUserEmail();
        return ResponseEntity.ok(taskMapper.toTaskDTOList(tasks));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTask(
            @RequestParam("id") @NotBlank String id){

        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }

    // To be removed (will be done internally once proper asynch communication with Notifier ms is implemented)
    @PatchMapping("/status")
    public ResponseEntity<TaskDTO> updateTaskStatus(
            @RequestParam("status") @NotNull NotificationStatusEnum notificationStatusEnum,
            @RequestParam("id") @NotBlank String id) {

        Task task = taskService.updateTaskStatus(notificationStatusEnum, id);
        return ResponseEntity.ok(taskMapper.toTaskDTO(task));
    }

    @PatchMapping
    public ResponseEntity<TaskDTO> updateTask(
            @RequestBody @Valid UpdateTaskRequestDTO taskDTO,
            @RequestParam("id") @NotBlank String id) {

        Task task = taskService.updateTask(taskMapper.fromUpdateTaskRequestDTO(taskDTO), id);
        return ResponseEntity.ok(taskMapper.toTaskDTO(task));
    }
}
