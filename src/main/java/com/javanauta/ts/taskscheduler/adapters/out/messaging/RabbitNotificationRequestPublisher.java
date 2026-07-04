package com.javanauta.ts.taskscheduler.adapters.out.messaging;

import com.javanauta.ts.events.messaging.Exchanges;
import com.javanauta.ts.events.messaging.RoutingKeys;
import com.javanauta.ts.events.notification.NotificationRequestEvent;
import com.javanauta.ts.taskscheduler.ports.out.messaging.NotificationRequestPublisher;
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
    public void publishNotificationRequest(NotificationRequestEvent event) {
        rabbitTemplate.convertAndSend(
                Exchanges.NOTIFICATION,
                RoutingKeys.NOTIFICATION_REQUEST,
                event
        );
    }
}
