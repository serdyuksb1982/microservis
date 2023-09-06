package ru.serdyuk.micro.planner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class PlannerConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlannerConfigApplication.class, args);
    }

}
