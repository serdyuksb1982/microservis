package ru.serdyuk.micro.planner.users.mq.legacy;

//import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

//@Component
//@EnableBinding(UserTodoBinding.class)//Класс отправки сообщений
public class MessageProducer {

    //private final UserTodoBinding userTodoBinding;

    //public MessageProducer(UserTodoBinding userTodoBinding) {
        //this.userTodoBinding = userTodoBinding;
    //}

    // отправка сообщения при создании нового user
   // public void initUserData(Long userId) {
        //контейнер для добавления данных и headers
       // Message message = MessageBuilder.withPayload(userId).build();

        //userTodoBinding.todoOutputChannel().send(message);
    //}
}
