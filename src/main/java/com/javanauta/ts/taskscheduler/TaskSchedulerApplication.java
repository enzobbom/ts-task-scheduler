package com.javanauta.ts.taskscheduler;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.mongodb.autoconfigure.MongoAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = MongoAutoConfiguration.class)
@EnableFeignClients
@EnableScheduling
@EnableRabbit
public class TaskSchedulerApplication {
	public static void main(String[] args) {
		SpringApplication.run(TaskSchedulerApplication.class, args);
	}
}
