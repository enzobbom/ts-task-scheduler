package com.javanauta.ts.taskscheduler.application.data;

import com.javanauta.ts.taskscheduler.application.data.enums.NotificationResult;

public record NotificationResultDetails(
        String taskId,
        NotificationResult notificationResult,
        String errorMessage
) {
}
