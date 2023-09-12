package ru.serdyuk.micro.planner.todo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.serdyuk.micro.planner.todo.service.TestDataService;

@RestController
@RequestMapping("/data")// базовый URI
public class TestDataController {

    private final TestDataService testDataService;

    public TestDataController(TestDataService testDataService) {
        this.testDataService = testDataService;
    }

    @PostMapping("/init")
    public ResponseEntity<Boolean> init(@RequestBody Long userId) {

        testDataService.initTestData(userId);

        // if user not found
        return ResponseEntity.ok(true);
    }
}
