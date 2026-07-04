package com.javanauta.ts.taskscheduler.adapters.in.messaging;

import com.javanauta.ts.events.messaging.Queues;
import com.javanauta.ts.events.notification.NotificationCompletedEvent;
import com.javanauta.ts.taskscheduler.application.service.TaskService;
import com.javanauta.ts.taskscheduler.shared.exception.ApplicationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class RabbitNotificationCompletedListener {

    private final TaskService taskService;

    @RabbitListener(queues = Queues.NOTIFICATION_COMPLETED)
    public void handleNotificationCompleted(NotificationCompletedEvent event) {
        try {
            taskService.processTaskNotificationCompletion(event);
        } catch (
                ApplicationException ex) {
            // No retryable exceptions. If any exception is thrown, is probably database related and could be recovered
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }
}
