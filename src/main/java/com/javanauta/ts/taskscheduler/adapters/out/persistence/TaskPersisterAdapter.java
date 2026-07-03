package com.javanauta.ts.taskscheduler.adapters.out.persistence;

import com.javanauta.ts.taskscheduler.domain.model.Task;
import com.javanauta.ts.taskscheduler.ports.out.persistence.TaskPersister;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TaskPersisterAdapter implements TaskPersister {
    private final MongoTaskRepository taskRepository;

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public void deleteById(String id) {
        taskRepository.deleteById(id);
    }

    @Override
    public Optional<Task> findById(String id) {
        return taskRepository.findById(id);
    }

    @Override
    public List<Task> findByScheduledDateTimeBetween(Instant initialDateTime, Instant finalDateTime) {
        return taskRepository.findByScheduledDateTimeBetween(initialDateTime, finalDateTime);
    }

    @Override
    public List<Task> findByUserEmail(String userEmail) {
        return taskRepository.findByUserEmail(userEmail);
    }
}
