package com.javanauta.ts.taskscheduler.adapters.in.messaging;

import com.javanauta.ts.events.notification.messaging.Queues;
import com.javanauta.ts.events.notification.NotificationFailedEvent;
import com.javanauta.ts.taskscheduler.adapters.in.messaging.validation.NotificationEventValidator;
import com.javanauta.ts.taskscheduler.application.data.NotificationResultDetails;
import com.javanauta.ts.taskscheduler.application.data.enums.NotificationResult;
import com.javanauta.ts.taskscheduler.application.service.TaskService;
import com.javanauta.ts.taskscheduler.shared.exception.ApplicationException;
import jakarta.validation.ConstraintViolationException;
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
    private final NotificationEventValidator notificationEventValidator;

    @RabbitListener(queues = Queues.NOTIFICATION_FAILED)
    public void handleNotificationFailed(NotificationFailedEvent event) {
        try {
            notificationEventValidator.validate(event);
        } catch (ConstraintViolationException ex) {
            throw new AmqpRejectAndDontRequeueException(ex);
        }

        NotificationResult result = switch (event.failureType()) {
            case PERMANENT -> NotificationResult.PERMANENT_FAILURE;
            case TEMPORARY -> NotificationResult.TEMPORARY_FAILURE;
        };

        NotificationResultDetails resultDetails = new NotificationResultDetails(
                event.taskId(),
                result,
                event.error());

        try {
            taskService.processTaskNotificationFailure(resultDetails);
        } catch (ApplicationException ex) {
            // No retryable exceptions. If any exception is thrown, is probably database related and could be recovered
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }
}