FROM gradle:jdk17 as builder
WORKDIR /app

COPY gradle ./gradle
COPY src ./src
COPY build.gradle settings.gradle ./

RUN gradle build -x test

FROM openjdk:17-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]