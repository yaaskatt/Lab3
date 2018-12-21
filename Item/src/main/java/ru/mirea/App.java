package ru.mirea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.*;


@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class);
        ApplicationContext context = new AnnotationConfigApplicationContext(App.class);
        PetService petService = context.getBean(PetService.class);
        petService.createDB();
        StuffService stuffService = context.getBean(StuffService.class);
        stuffService.createDB();
    }
}

