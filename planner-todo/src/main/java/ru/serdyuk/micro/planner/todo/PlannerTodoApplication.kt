package ru.serdyuk.micro.planner.todo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = ["ru.serdyuk.micro.planner"]) // указание Spring откуда начинать поиск бинов
@EnableJpaRepositories("ru.serdyuk.micro.planner.todo") // для конкретизации пути поиска репозиториев в пакете todo
@EnableFeignClients
@RefreshScope //позволяет динамически изменять состав бина - из файлов конфигурации, в процессе использования
class PlannerTodoApplication

    fun main(args: Array<String>) {
        runApplication<PlannerTodoApplication>(*args)
    }
