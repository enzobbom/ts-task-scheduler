package com.javanauta.taskscheduler.infrastructure.entity;

import com.javanauta.taskscheduler.infrastructure.enums.NotificationStatusEnum;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("task")
public class Task {

    @Id
    private String id;
    private String name;
    private String description;
    private LocalDateTime creationDateTime;
    private LocalDateTime dueDateTime;
    private String userEmail;
    private LocalDateTime modificationDateTime;
    private NotificationStatusEnum notificationStatusEnum;
}
