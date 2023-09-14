package ru.serdyuk.micro.planner.todo.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.serdyuk.micro.planner.todo.service.StatService

@RestController
class StatController(private val statService: StatService) {
    @PostMapping("/stat")
    fun findByEmail(@RequestBody userId: Long): ResponseEntity<Any> {
        return ResponseEntity.ok(statService.findStat(userId))
    }
}