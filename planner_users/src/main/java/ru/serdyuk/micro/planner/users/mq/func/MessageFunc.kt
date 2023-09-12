package ru.serdyuk.micro.planner.users.mq.func;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

import java.util.function.Supplier;

@Configuration// конфигурационный класс
public class MessageFunc {
    // для считывания данных по требованию, а не постоянно в потоке
    // внутренняя шина, из которой будут отправляться сообщения в канал SCS (по требованию)
    private Sinks.Many<Message<Long>> innerBus = Sinks
            .many()//который определяет уведомлене всех слушателей
            .multicast()//отправка сигнала всем подписчикам
            .onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);//начальные параметры - максимальное ко-во сообщений (16), autoCancel - false при отписывании всех подписчиков, труба не закроется

    public Sinks.Many<Message<Long>> getInnerBus() {
        return innerBus;
    }

    // отправляет в канал id пользователя, для которого нужно создать тестовые данные
    // название метода должно совпадать с настройками definition и bindings в файлах .properties
    @Bean
    public Supplier<Flux<Message<Long>>> newUserActionProduce() {
        return () -> innerBus.asFlux();
    }
}
