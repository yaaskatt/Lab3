package ru.mirea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;


@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class);
        ApplicationContext context = new AnnotationConfigApplicationContext(App.class);
        BalanceService balanceService = context.getBean(BalanceService.class);
        balanceService.createDB();
    }
}

