package ru.serdyuk.micro.planner.users.mq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface UserTodoBinding {

    String OUTPUT_CHANNEL = "todoOutputChannel";

    // создаем канал для отправки сообщений
    @Output(OUTPUT_CHANNEL)
    MessageChannel todoOutputChannel();
}
