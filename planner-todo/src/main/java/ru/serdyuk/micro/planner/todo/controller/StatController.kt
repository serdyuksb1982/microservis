package ru.serdyuk.micro.planner.todo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.serdyuk.micro.planner.entity.Stat;
import ru.serdyuk.micro.planner.todo.service.StatService;

@RestController
public class StatController {

    private final StatService statService;

    public StatController(StatService statService) {
        this.statService = statService;
    }

    @PostMapping("/stat")
    public ResponseEntity<Stat> findByEmail(@RequestBody Long userId) {
        return ResponseEntity.ok(statService.findStat(userId));
    }

}
