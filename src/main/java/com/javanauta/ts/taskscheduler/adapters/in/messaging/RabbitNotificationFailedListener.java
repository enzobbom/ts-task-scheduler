package com.javanauta.ts.taskscheduler.adapters.in.messaging;

import com.javanauta.ts.events.messaging.Queues;
import com.javanauta.ts.events.notification.NotificationFailedEvent;
import com.javanauta.ts.taskscheduler.application.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitNotificationFailedListener {

    private final TaskService taskService;

    @RabbitListener(queues = Queues.NOTIFICATION_FAILED)
    public void handleNotificationFailed(NotificationFailedEvent event) {
        taskService.processTaskNotificationFailure(event);
    }
}