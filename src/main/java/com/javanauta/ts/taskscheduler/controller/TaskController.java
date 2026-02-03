package com.javanauta.ts.taskscheduler.controller;

import com.javanauta.ts.taskscheduler.business.TaskService;
import com.javanauta.ts.taskscheduler.business.dto.TaskDTO;
import com.javanauta.ts.taskscheduler.infrastructure.enums.NotificationStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDTO> saveTask(@RequestBody TaskDTO taskDTO, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(taskService.saveTask(token, taskDTO));
    }

    @GetMapping("/events")
    public ResponseEntity<List<TaskDTO>> findTaskListByPeriod(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant initialDateTime, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant finalDateTime) {
        return ResponseEntity.ok(taskService.findTaskByTimePeriod(initialDateTime, finalDateTime));
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> findTaskListByUserEmail(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(taskService.findTaskByUserEmail(token));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTaskById(@RequestParam("id") String id){
        taskService.deleteTaskById(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<TaskDTO> modifyTaskStatusById(@RequestParam("status") NotificationStatusEnum notificationStatusEnum, @RequestParam("id") String id) {
        return ResponseEntity.ok(taskService.modifyTaskStatusById(notificationStatusEnum, id));
    }

    @PutMapping
    public ResponseEntity<TaskDTO> updateTaskById(@RequestBody TaskDTO taskDTO, @RequestParam("id") String id) {
        return ResponseEntity.ok(taskService.updateTaskById(taskDTO, id));
    }
}
