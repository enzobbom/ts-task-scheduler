package com.javanauta.ts.taskscheduler.adapters.out.persistence;

import com.javanauta.ts.taskscheduler.domain.model.Task;
import com.javanauta.ts.taskscheduler.domain.model.enums.NotificationStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface MongoTaskRepository extends MongoRepository<Task, String> {
    List<Task> findByNotificationStatusInAndScheduledDateTimeBetween(Collection<NotificationStatus> statuses, Instant initialDateTime, Instant finalDateTime);
    List<Task> findByUserId(UUID userId);
}
