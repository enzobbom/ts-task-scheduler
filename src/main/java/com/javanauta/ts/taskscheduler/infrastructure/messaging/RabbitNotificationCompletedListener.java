package com.javanauta.ts.taskscheduler.infrastructure.messaging;

import com.javanauta.ts.events.messaging.Queues;
import com.javanauta.ts.events.notification.NotificationCompletedEvent;
import com.javanauta.ts.taskscheduler.application.ports.NotificationCompletedListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@RabbitListener(queues = Queues.NOTIFICATION_COMPLETED)
public class RabbitNotificationCompletedListener implements NotificationCompletedListener {
    public void completed(NotificationCompletedEvent event) {
    }
}
