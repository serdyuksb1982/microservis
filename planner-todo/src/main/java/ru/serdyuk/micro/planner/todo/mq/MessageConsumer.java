package ru.serdyuk.micro.planner.todo.mq;


import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import ru.serdyuk.micro.planner.todo.service.TestDataService;

@EnableBinding(TodoBinding.class)
@Component
public class MessageConsumer {

    private final TestDataService testDataService;

    public MessageConsumer(TestDataService testDataService) {
        this.testDataService = testDataService;
    }


    @StreamListener(target = TodoBinding.INPUT_CHANNEL)
    public void initTestData(Long userId) {
        testDataService.initTestData(userId);
    }
}
