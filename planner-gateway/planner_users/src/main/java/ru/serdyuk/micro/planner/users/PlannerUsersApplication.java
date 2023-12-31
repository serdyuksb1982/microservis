package ru.serdyuk.micro.planner.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = {"ru.serdyuk.micro.planner"})
@EnableJpaRepositories(basePackages = {"ru.serdyuk.micro.planner.users"})
public class PlannerUsersApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlannerUsersApplication.class, args);
    }

}
