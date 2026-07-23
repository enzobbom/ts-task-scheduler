package com.javanauta.ts.taskscheduler.adapters.in.web.controller;

import com.javanauta.ts.apicontract.response.ErrorResponse;
import com.javanauta.ts.apicontract.response.SuccessResponse;
import com.javanauta.ts.taskscheduler.adapters.in.security.config.SecurityConfig;
import com.javanauta.ts.taskscheduler.adapters.in.web.dto.in.CreateTaskRequestDTO;
import com.javanauta.ts.taskscheduler.adapters.in.web.dto.in.UpdateTaskRequestDTO;
import com.javanauta.ts.taskscheduler.adapters.in.web.dto.out.TaskResponseDTO;
import com.javanauta.ts.taskscheduler.adapters.in.web.mapper.TaskMapper;
import com.javanauta.ts.taskscheduler.adapters.in.web.path.ApiPaths;
import com.javanauta.ts.taskscheduler.application.service.TaskService;
import com.javanauta.ts.taskscheduler.domain.model.Task;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME)
@Tag(name = "Tasks", description = "Operations for managing the authenticated user's tasks")
@Validated
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @PostMapping
    @Operation(summary = "Create task", description = "Creates a new task for the authenticated user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task successfully created"),
            @ApiResponse(responseCode = "401", description = "Authentication failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Request body validation failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<SuccessResponse<TaskResponseDTO>> createTask(
            @RequestBody @Valid CreateTaskRequestDTO createTaskRequestDTO) {

        Task createdTask = taskService.createTask(taskMapper.fromCreateTaskRequestDTO(createTaskRequestDTO));

        HttpStatus httpCode = HttpStatus.OK;
        SuccessResponse<TaskResponseDTO> successResponse = new SuccessResponse<>(
                httpCode.value(),
                taskMapper.toTaskDTO(createdTask));

        return ResponseEntity.status(httpCode).body(successResponse);
    }

    @GetMapping
    @Operation(summary = "Get user tasks", description = "Returns all tasks belonging to the authenticated user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tasks successfully retrieved"),
            @ApiResponse(responseCode = "401", description = "Authentication failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<SuccessResponse<List<TaskResponseDTO>>> getTasks() {
        List<Task> tasks = taskService.getTasks();

        HttpStatus httpCode = HttpStatus.OK;
        SuccessResponse<List<TaskResponseDTO>> successResponse = new SuccessResponse<>(
                httpCode.value(),
                taskMapper.toTaskDTOList(tasks));

        return ResponseEntity.status(httpCode).body(successResponse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task", description = "Deletes a task owned by the authenticated user")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task successfully deleted"),
            @ApiResponse(responseCode = "401", description = "Authentication failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "User does not own the requested task",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Request parameter validation failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> deleteTask(
            @PathVariable @NotBlank String id) {

        taskService.deleteTask(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Update task",
            description = """
                    Updates a task owned by the authenticated user.
                    
                    PATCH behavior:
                    > Missing fields are ignored \n\n
                    > Fields sent as null are ignored \n\n
                    > Send an empty string ("") to clear optional String fields ('description')
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task successfully updated"),
            @ApiResponse(responseCode = "401", description = "Authentication failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "User does not own the requested task",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Request validation failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<SuccessResponse<TaskResponseDTO>> updateTask(
            @RequestBody @Valid UpdateTaskRequestDTO taskDTO,
            @PathVariable @NotBlank String id) {

        Task task = taskService.updateTask(taskMapper.fromUpdateTaskRequestDTO(taskDTO), id);

        HttpStatus httpCode = HttpStatus.OK;
        SuccessResponse<TaskResponseDTO> successResponse = new SuccessResponse<>(
                httpCode.value(),
                taskMapper.toTaskDTO(task));

        return ResponseEntity.status(httpCode).body(successResponse);
    }
}
