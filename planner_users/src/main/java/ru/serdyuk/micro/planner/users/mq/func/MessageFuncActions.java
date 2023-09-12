package ru.serdyuk.micro.planner.users.mq.func;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
//помогает реализовать отправку сообщения с помощью функ. кода - по требованию
public class MessageFuncActions {
    //каналы для обмена сообщениями
    private MessageFunc messageFunc;

    private MessageFunc getMessageFunc() {
        return messageFunc;
    }

    public MessageFuncActions(MessageFunc streamFunctions) {
        this.messageFunc = streamFunctions;
    }

    public void sendNewUserMessage(Long id) {
        //добавляем в слушатель новое сообщение
        messageFunc.getInnerBus()
                .emitNext(MessageBuilder.withPayload(id).build(), //помещаем сообщение в шину
                        Sinks.EmitFailureHandler.FAIL_FAST //обработка ошибок
                );
        System.out.println("Message send: " + id);
    }
}
