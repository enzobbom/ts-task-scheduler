package com.javanauta.ts.taskscheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.mongodb.autoconfigure.MongoAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = MongoAutoConfiguration.class)
@EnableFeignClients
public class TaskSchedulerApplication {
	public static void main(String[] args) {
		SpringApplication.run(TaskSchedulerApplication.class, args);
	}
}
