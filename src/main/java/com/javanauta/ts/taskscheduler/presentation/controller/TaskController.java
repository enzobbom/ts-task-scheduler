package com.javanauta.ts.taskscheduler.presentation.controller;

import com.javanauta.ts.taskscheduler.application.service.TaskService;
import com.javanauta.ts.taskscheduler.domain.model.Task;
import com.javanauta.ts.taskscheduler.domain.model.enums.NotificationStatus;
import com.javanauta.ts.taskscheduler.presentation.dto.in.CreateTaskRequestDTO;
import com.javanauta.ts.taskscheduler.presentation.dto.in.UpdateTaskRequestDTO;
import com.javanauta.ts.taskscheduler.presentation.mapper.TaskMapper;
import com.javanauta.ts.taskscheduler.presentation.response.SuccessResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<SuccessResponse> createTask(
            @RequestBody @Valid CreateTaskRequestDTO createTaskRequestDTO) {

        Task createdTask = taskService.createTask(taskMapper.fromCreateTaskRequestDTO(createTaskRequestDTO));

        HttpStatus httpCode = HttpStatus.OK;
        SuccessResponse successResponse = new SuccessResponse(
                httpCode,
                taskMapper.toTaskDTO(createdTask));

        return ResponseEntity.status(httpCode).body(successResponse);
    }

    // To be removed (and used internally only)
    @GetMapping("/events")
    public ResponseEntity<SuccessResponse> findTasksByPeriod(
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant initialDateTime,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant finalDateTime) {

        List<Task> tasks = taskService.findTasksByTimePeriod(initialDateTime, finalDateTime);

        HttpStatus httpCode = HttpStatus.OK;
        SuccessResponse successResponse = new SuccessResponse(
                httpCode,
                taskMapper.toTaskDTOList(tasks));

        return ResponseEntity.status(httpCode).body(successResponse);
    }

    @GetMapping
    public ResponseEntity<SuccessResponse> findTasksByUserEmail() {

        List<Task> tasks = taskService.findTasksByUserEmail();

        HttpStatus httpCode = HttpStatus.OK;
        SuccessResponse successResponse = new SuccessResponse(
                httpCode,
                taskMapper.toTaskDTOList(tasks));

        return ResponseEntity.status(httpCode).body(successResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTask(
            @RequestParam("id") @NotBlank String id) {

        taskService.deleteTask(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // To be removed (will be done internally once proper asynch communication with Notifier ms is implemented)
    @PatchMapping("/status")
    public ResponseEntity<SuccessResponse> updateTaskStatus(
            @RequestParam("status") @NotNull NotificationStatus notificationStatus,
            @RequestParam("id") @NotBlank String id) {

        Task task = taskService.updateTaskStatus(notificationStatus, id);

        HttpStatus httpCode = HttpStatus.OK;
        SuccessResponse successResponse = new SuccessResponse(
                httpCode,
                taskMapper.toTaskDTO(task));

        return ResponseEntity.status(httpCode).body(successResponse);
    }

    @PatchMapping
    public ResponseEntity<SuccessResponse> updateTask(
            @RequestBody @Valid UpdateTaskRequestDTO taskDTO,
            @RequestParam("id") @NotBlank String id) {

        Task task = taskService.updateTask(taskMapper.fromUpdateTaskRequestDTO(taskDTO), id);

        HttpStatus httpCode = HttpStatus.OK;
        SuccessResponse successResponse = new SuccessResponse(
                httpCode,
                taskMapper.toTaskDTO(task));

        return ResponseEntity.status(httpCode).body(successResponse);
    }
}
