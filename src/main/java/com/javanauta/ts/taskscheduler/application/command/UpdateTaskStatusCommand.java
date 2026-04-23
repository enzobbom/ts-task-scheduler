package com.javanauta.ts.taskscheduler.application.command;

import com.javanauta.ts.taskscheduler.domain.model.enums.NotificationStatusEnum;
import lombok.Builder;

@Builder
public record UpdateTaskStatusCommand(
        NotificationStatusEnum notificationStatusEnum) {
}
