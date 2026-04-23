package com.javanauta.ts.taskscheduler.presentation.dto.in;

import com.javanauta.ts.taskscheduler.domain.model.enums.NotificationStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record UpdateTaskStatusRequestDTO(
        @NotNull NotificationStatusEnum notificationStatusEnum) {
}
