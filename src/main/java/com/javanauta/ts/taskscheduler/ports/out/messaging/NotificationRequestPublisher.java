package com.javanauta.ts.taskscheduler.ports.out.messaging;

import com.javanauta.ts.events.notification.NotificationRequestEvent;

public interface NotificationRequestPublisher {
    void publishNotificationRequest(NotificationRequestEvent event);
}
