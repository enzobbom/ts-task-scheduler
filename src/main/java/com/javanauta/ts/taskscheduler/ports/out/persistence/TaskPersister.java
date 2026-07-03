package com.javanauta.ts.taskscheduler.ports.out.persistence;

import com.javanauta.ts.taskscheduler.domain.model.Task;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TaskPersister {
    Task save(Task task);
    void deleteById(String id);
    Optional<Task> findById(String id);
    List<Task> findByScheduledDateTimeBetween(Instant initialDateTime, Instant finalDateTime);
    List<Task> findByUserEmail(String userEmail);
}
