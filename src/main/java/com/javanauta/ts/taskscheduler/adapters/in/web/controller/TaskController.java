package com.javanauta.ts.taskscheduler.adapters.in.web.controller;

import com.javanauta.ts.apicontract.response.SuccessResponse;
import com.javanauta.ts.taskscheduler.adapters.in.web.dto.in.CreateTaskRequestDTO;
import com.javanauta.ts.taskscheduler.adapters.in.web.dto.in.UpdateTaskRequestDTO;
import com.javanauta.ts.taskscheduler.adapters.in.web.mapper.TaskMapper;
import com.javanauta.ts.taskscheduler.adapters.in.web.path.ApiPaths;
import com.javanauta.ts.taskscheduler.application.service.TaskService;
import com.javanauta.ts.taskscheduler.domain.model.Task;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.TASKS_V1)
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
                httpCode.value(),
                taskMapper.toTaskDTO(createdTask));

        return ResponseEntity.status(httpCode).body(successResponse);
    }

    @GetMapping
    public ResponseEntity<SuccessResponse> findTasksByUserEmail() {
        List<Task> tasks = taskService.findTasksByUserEmail();

        HttpStatus httpCode = HttpStatus.OK;
        SuccessResponse successResponse = new SuccessResponse(
                httpCode.value(),
                taskMapper.toTaskDTOList(tasks));

        return ResponseEntity.status(httpCode).body(successResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable @NotBlank String id) {

        taskService.deleteTask(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SuccessResponse> updateTask(
            @RequestBody @Valid UpdateTaskRequestDTO taskDTO,
            @PathVariable @NotBlank String id) {

        Task task = taskService.updateTask(taskMapper.fromUpdateTaskRequestDTO(taskDTO), id);

        HttpStatus httpCode = HttpStatus.OK;
        SuccessResponse successResponse = new SuccessResponse(
                httpCode.value(),
                taskMapper.toTaskDTO(task));

        return ResponseEntity.status(httpCode).body(successResponse);
    }
}
