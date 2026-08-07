package com.javanauta.ts.taskscheduler.adapters.in.messaging;

import com.javanauta.ts.events.notification.NotificationEvent;
import com.javanauta.ts.taskscheduler.shared.exception.ApplicationException;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class NotificationEventRecoverer implements MessageRecoverer {
    private final MessageConverter messageConverter;

    @Override
    public void recover(Message message, Throwable cause) {
        // Pre-listener conversion failure
        if (findCause(cause, MessageConversionException.class) != null) {
            log.error("""
                    Invalid Event received.
                    Payload:
                    {}
                    """, new String(message.getBody(), StandardCharsets.UTF_8), cause);
            return;
        }

        NotificationEvent event;
        try {
            event = (NotificationEvent) messageConverter.fromMessage(message);
        } catch (MessageConversionException e) {
            log.error("""
                    Attempt to deserialize Event within the Recoverer failed.
                    Payload:
                    {}
                    """, new String(message.getBody(), StandardCharsets.UTF_8), e);
            return;
        }

        // Validation errors
        ConstraintViolationException constraintViolationException = findCause(cause, ConstraintViolationException.class);
        if (constraintViolationException != null) {
            List<String> constraintMessages = constraintViolationException
                    .getConstraintViolations()
                    .stream()
                    .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                    .toList();

            log.error("Validation of {} for Task {} has failed: {}",
                    event.getClass().getSimpleName(),
                    event.taskId(),
                    String.join(", ", constraintMessages));

            return;
        }

        // Business error
        String errorMsg;
        ApplicationException appException = findCause(cause, ApplicationException.class);
        if (appException != null) {
            log.error("Processing of {} for Task {} has failed due to a business error ({}): {}",
                    event.getClass().getSimpleName(),
                    event.taskId(),
                    appException.getCode().getIdentifier(),
                    appException.getMessage());
            return;
        }

        // Any other unhandled exception
        log.error("Processing of {} for Task {} has failed due to an internal error",
                event.getClass().getSimpleName(),
                event.taskId(),
                cause);
    }

    private <T extends Throwable> T findCause(Throwable throwable, Class<T> type) {
        while (throwable != null) {
            if (type.isInstance(throwable)) {
                return type.cast(throwable);
            }
            throwable = throwable.getCause();
        }
        return null;
    }
}
