package com.javanauta.ts.taskscheduler.ports.out.persistence;

import com.javanauta.ts.taskscheduler.domain.model.Task;
import com.javanauta.ts.taskscheduler.domain.model.enums.NotificationStatus;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskPersister {
    Task save(Task task);
    void deleteById(String id);
    Optional<Task> findById(String id);
    List<Task> findByNotificationStatusInAndScheduledDateTimeBetween(Collection<NotificationStatus> statuses, Instant initialDateTime, Instant finalDateTime);
    List<Task> findByUserId(UUID userId);
}
