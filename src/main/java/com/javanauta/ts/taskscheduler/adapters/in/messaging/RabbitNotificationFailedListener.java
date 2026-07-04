package com.javanauta.ts.taskscheduler.adapters.in.messaging;

import com.javanauta.ts.events.messaging.Queues;
import com.javanauta.ts.events.notification.NotificationFailedEvent;
import com.javanauta.ts.taskscheduler.application.service.TaskService;
import com.javanauta.ts.taskscheduler.shared.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitNotificationFailedListener {

    private final TaskService taskService;

    @RabbitListener(queues = Queues.NOTIFICATION_FAILED)
    public void handleNotificationFailed(NotificationFailedEvent event) {
        try {
            taskService.processTaskNotificationFailure(event);
        } catch (ApplicationException ex) {
            // No retryable exceptions. If any exception is thrown, is probably database related and could be recovered
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }
}