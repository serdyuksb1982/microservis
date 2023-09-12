package ru.serdyuk.micro.planner.todo.mq.legacy;


import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import ru.serdyuk.micro.planner.todo.service.TestDataService;

//@EnableBinding(TodoBinding.class)
//@Component// класс получения сообщений
public class MessageConsumer {

    /*private final TestDataService testDataService;

    public MessageConsumer(TestDataService testDataService) {
        this.testDataService = testDataService;
    }


    @StreamListener(target = TodoBinding.INPUT_CHANNEL)
    public void initTestData(Long userId) throws Exception {
        //throw new Exception("test DQL"); тестовая проверка Dead letter Queue, для этого нужно удалить прошлую очередь через Админ консоль RabbitMQ
        testDataService.initTestData(userId);
    }*/
}
