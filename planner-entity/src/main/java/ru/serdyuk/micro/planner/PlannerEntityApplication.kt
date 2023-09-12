package ru.serdyuk.micro.planner

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PlannerEntityApplication

    fun main(args: Array<String>) {
        runApplication<PlannerEntityApplication>(*args)
    }
