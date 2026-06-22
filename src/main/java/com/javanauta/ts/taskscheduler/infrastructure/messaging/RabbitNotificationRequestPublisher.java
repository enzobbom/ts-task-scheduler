package com.javanauta.ts.taskscheduler.infrastructure.messaging;

import com.javanauta.ts.events.messaging.Exchanges;
import com.javanauta.ts.events.messaging.RoutingKeys;
import com.javanauta.ts.events.notification.NotificationRequestedEvent;
import com.javanauta.ts.taskscheduler.application.ports.NotificationRequestPublisher;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@AllArgsConstructor
public class RabbitNotificationRequestPublisher implements NotificationRequestPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publish(NotificationRequestedEvent event) {
        rabbitTemplate.convertAndSend(
                Exchanges.NOTIFICATION,
                RoutingKeys.NOTIFICATION_REQUEST,
                event
        );
    }
}
