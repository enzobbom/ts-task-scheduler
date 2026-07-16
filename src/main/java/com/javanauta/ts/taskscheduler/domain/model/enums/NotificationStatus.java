package com.javanauta.ts.taskscheduler.domain.model.enums;

import java.util.Set;

public enum NotificationStatus {
    PENDING,
    DISPATCHED,
    NOTIFIED,
    PENDING_RETRY,
    FAILED;

    public static Set<NotificationStatus> notifiableStatuses() {
        return Set.of(PENDING, PENDING_RETRY);
    }
}
