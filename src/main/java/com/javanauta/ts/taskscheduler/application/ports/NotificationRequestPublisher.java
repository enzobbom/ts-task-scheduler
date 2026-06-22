package com.javanauta.ts.taskscheduler.application.ports;

import com.javanauta.ts.events.notification.NotificationRequestedEvent;

public interface NotificationRequestPublisher {
    void publish(NotificationRequestedEvent event);
}
