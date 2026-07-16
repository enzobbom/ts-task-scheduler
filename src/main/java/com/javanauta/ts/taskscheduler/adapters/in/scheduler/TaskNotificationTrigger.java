package com.javanauta.ts.taskscheduler.adapters.in.scheduler;

import com.javanauta.ts.taskscheduler.application.service.TaskService;
import com.javanauta.ts.taskscheduler.domain.model.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskNotificationTrigger {
    private final TaskService taskService;

    @Scheduled(cron = "${ts.scheduler.frequency}")
    public void findTasksInNextNotificationPeriod() {
        long notificationPeriod = 1L; // in hours - currently hard coded but meant to be task setting

        Instant nowTime = Instant.now();
        List<Task> tasksList = taskService.findTasksToNotify(nowTime, nowTime.plus(notificationPeriod, ChronoUnit.HOURS));
        tasksList.forEach(taskService::requestTaskNotification);
    }
}
