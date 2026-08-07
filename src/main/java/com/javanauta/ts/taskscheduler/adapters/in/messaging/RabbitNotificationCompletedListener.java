package com.javanauta.ts.taskscheduler.adapters.in.messaging;

import com.javanauta.ts.events.notification.NotificationCompletedEvent;
import com.javanauta.ts.events.notification.messaging.Queues;
import com.javanauta.ts.taskscheduler.adapters.in.messaging.validation.NotificationEventValidator;
import com.javanauta.ts.taskscheduler.application.data.NotificationResultDetails;
import com.javanauta.ts.taskscheduler.application.data.enums.NotificationResult;
import com.javanauta.ts.taskscheduler.application.service.TaskService;
import com.javanauta.ts.taskscheduler.shared.exception.ApplicationException;
import jakarta.validation.ConstraintViolationException;
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
    private final NotificationEventValidator notificationEventValidator;

    @RabbitListener(queues = Queues.NOTIFICATION_COMPLETED)
    public void handleNotificationCompleted(NotificationCompletedEvent event) {
        try {
            notificationEventValidator.validate(event);
        } catch (ConstraintViolationException ex) {
            throw new AmqpRejectAndDontRequeueException(ex);
        }

        NotificationResultDetails resultDetails = new NotificationResultDetails(
                event.taskId(),
                NotificationResult.SUCCESS,
                null);

        try {
            taskService.processTaskNotificationCompletion(resultDetails);
        } catch (ApplicationException ex) {
            // No retryable exceptions. If any exception is thrown, is probably database related and could be recovered
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }
}
