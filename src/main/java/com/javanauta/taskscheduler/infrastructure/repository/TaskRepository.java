package com.javanauta.taskscheduler.infrastructure.repository;

import com.javanauta.taskscheduler.infrastructure.entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByDueDateTimeBetween(LocalDateTime initialDateTime, LocalDateTime finalDateTime);
    List<Task> findByUserEmail(String userEmail);
}
