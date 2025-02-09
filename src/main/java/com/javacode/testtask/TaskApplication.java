package com.javacode.testtask;

import jakarta.annotation.Nullable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskApplication {
    public static void main(@Nullable final String[] args) {
        SpringApplication.run(TaskApplication.class, args);
    }
}
