package com.javanauta.ts.taskscheduler.presentation.controller;

import com.javanauta.ts.taskscheduler.application.service.TaskService;
import com.javanauta.ts.taskscheduler.domain.model.Task;
import com.javanauta.ts.taskscheduler.domain.model.enums.NotificationStatusEnum;
import com.javanauta.ts.taskscheduler.presentation.dto.TaskDTO;
import com.javanauta.ts.taskscheduler.presentation.dto.in.CreateTaskRequestDTO;
import com.javanauta.ts.taskscheduler.presentation.mapper.TaskMapper;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody CreateTaskRequestDTO createTaskRequestDTO, @RequestHeader("Authorization") String token) {
        Task createdTask = taskService.createTask(token, taskMapper.fromCreateTaskRequestDTO(createTaskRequestDTO));
        return ResponseEntity.ok(taskMapper.toTaskDTO(createdTask));
    }

    @GetMapping("/events")
    public ResponseEntity<List<TaskDTO>> findTaskListByPeriod(@RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant initialDateTime, @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant finalDateTime) {
        List<Task> tasks = taskService.findTaskByTimePeriod(initialDateTime, finalDateTime);
        return ResponseEntity.ok(taskMapper.toTaskDTOList(tasks));
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> findTaskListByUserEmail(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(taskService.findTaskByUserEmail(token));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTaskById(@RequestParam("id") String id){
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<TaskDTO> updateTaskStatus(@RequestParam("status") NotificationStatusEnum notificationStatusEnum, @RequestParam("id") String id) {
        return ResponseEntity.ok(taskService.updateTaskStatus(notificationStatusEnum, id));
    }

    @PutMapping
    public ResponseEntity<TaskDTO> updateTask(@RequestBody TaskDTO taskDTO, @RequestParam("id") String id) {
        return ResponseEntity.ok(taskService.updateTask(taskDTO, id));
    }
}
