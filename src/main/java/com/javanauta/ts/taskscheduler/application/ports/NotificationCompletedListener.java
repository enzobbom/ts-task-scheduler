package com.javanauta.ts.taskscheduler.application.ports;

import com.javanauta.ts.events.notification.NotificationCompletedEvent;

public interface NotificationCompletedListener {
    void completed(NotificationCompletedEvent event);
}
