package com.javanauta.ts.taskscheduler.ports.out.messaging;

import com.javanauta.ts.taskscheduler.domain.model.Task;

public interface NotificationRequestPublisher {
    void publishNotificationRequest(Task task);
}
