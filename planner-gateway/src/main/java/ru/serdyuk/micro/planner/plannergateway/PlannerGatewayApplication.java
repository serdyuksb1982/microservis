package ru.serdyuk.micro.planner.plannergateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class PlannerGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlannerGatewayApplication.class, args);
    }

}
