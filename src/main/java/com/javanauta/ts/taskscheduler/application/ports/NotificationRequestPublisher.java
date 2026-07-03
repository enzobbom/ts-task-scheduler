package com.javanauta.ts.taskscheduler.application.ports;

import com.javanauta.ts.events.notification.NotificationRequestEvent;

public interface NotificationRequestPublisher {
    void publishNotificationRequest(NotificationRequestEvent event);
}
