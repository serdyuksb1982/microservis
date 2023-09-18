package ru.serdyuk.micro.planner.users.mq.func

import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service
import reactor.core.publisher.Sinks

@Service //помогает реализовать отправку сообщения с помощью функ. кода - по требованию
class MessageFuncActions( //каналы для обмена сообщениями
   // private val messageFunc: MessageFunc
) {

    /*fun sendNewUserMessage(id: Long) {
        //добавляем в слушатель новое сообщение
        messageFunc.innerBus
            .emitNext(
                MessageBuilder.withPayload(id).build(),  //помещаем сообщение в шину
                Sinks.EmitFailureHandler.FAIL_FAST //обработка ошибок
            )
        println("Message send: $id")
    }*/
}