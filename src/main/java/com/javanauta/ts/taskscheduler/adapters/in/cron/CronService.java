package com.javanauta.ts.taskscheduler.adapters.in.cron;

import com.javanauta.ts.taskscheduler.application.service.TaskService;
import com.javanauta.ts.taskscheduler.domain.model.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CronService {
    private final TaskService taskService;

    @Scheduled(cron = "${ts.cron.frequency}")
    public void findTasksInNextNotificationPeriod() {
        long notificationPeriod = 1L; // in hours - currently hard coded but meant to be task setting

        Instant nowTime = Instant.now();
        List<Task> tasksList = taskService.findTasksByTimePeriod(nowTime, nowTime.plus(notificationPeriod, ChronoUnit.HOURS));
        tasksList.forEach(taskService::requestTaskNotification);
    }
}
