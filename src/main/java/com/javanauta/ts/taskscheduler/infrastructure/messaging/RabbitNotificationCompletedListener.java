package com.javanauta.ts.taskscheduler.infrastructure.messaging;

import com.javanauta.ts.events.messaging.Queues;
import com.javanauta.ts.events.notification.NotificationCompletedEvent;
import com.javanauta.ts.taskscheduler.application.ports.NotificationCompletedListener;
import com.javanauta.ts.taskscheduler.application.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class RabbitNotificationCompletedListener implements NotificationCompletedListener {

    private final TaskService taskService;

    @RabbitListener(queues = Queues.NOTIFICATION_COMPLETED)
    @Override
    public void handleNotificationCompleted(NotificationCompletedEvent event) {
        log.info(
                "Handling NotificationCompletedEvent for Task '{}'",
                event.taskId()
        );

        taskService.processTaskNotificationCompletion(event);

        log.info("Handled successfully");
    }
}
