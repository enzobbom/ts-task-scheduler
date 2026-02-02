# Basic Docker file
#FROM eclipse-temurin:17-jdk-jammy
#WORKDIR /app
#COPY build/libs/task-scheduler-0.0.1-SNAPSHOT.jar /app/task-scheduler.jar
#EXPOSE 8081
#CMD ["java", "-jar", "/app/task-scheduler.jar"]

# Build
FROM gradle:jdk17 AS build
WORKDIR /app
COPY . .

# Run .jar
RUN gradle build --no-daemon
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/task-scheduler.jar
EXPOSE 8081
CMD ["java", "-jar", "/app/task-scheduler.jar"]


