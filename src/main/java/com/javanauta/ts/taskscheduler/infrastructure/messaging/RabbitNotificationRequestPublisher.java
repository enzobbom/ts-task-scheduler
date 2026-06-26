package com.javanauta.ts.taskscheduler.infrastructure.messaging;

import com.javanauta.ts.events.messaging.Exchanges;
import com.javanauta.ts.events.messaging.RoutingKeys;
import com.javanauta.ts.events.notification.NotificationRequestedEvent;
import com.javanauta.ts.taskscheduler.application.ports.NotificationRequestPublisher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class RabbitNotificationRequestPublisher implements NotificationRequestPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishNotificationRequest(NotificationRequestedEvent event) {
        String exchangeName = Exchanges.NOTIFICATION;
        String routingKeyName = RoutingKeys.NOTIFICATION_REQUEST;

        log.info(
                "Publishing NoficationRequestEvent '{}' to exchange [{}] with routing key [{}] for Task '{}'",
                event.eventId(),
                exchangeName,
                routingKeyName,
                event.taskId()
        );

        rabbitTemplate.convertAndSend(
                exchangeName,
                routingKeyName,
                event
        );

        log.info("Published successfully");
    }
}
