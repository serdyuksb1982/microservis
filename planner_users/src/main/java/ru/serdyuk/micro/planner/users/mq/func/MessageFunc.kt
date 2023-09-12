package ru.serdyuk.micro.planner.users.mq.func

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import reactor.util.concurrent.Queues
import java.util.function.Supplier

@Configuration // конфигурационный класс
class MessageFunc {
    // для считывания данных по требованию, а не постоянно в потоке
    // внутренняя шина, из которой будут отправляться сообщения в канал SCS (по требованию)
    val innerBus: Sinks.Many<Message<Long>> = Sinks.many().multicast().onBackpressureBuffer<Message<Long>>(Queues.SMALL_BUFFER_SIZE, false) //начальные параметры - максимальное ко-во сообщений (16), autoCancel - false при отписывании всех подписчиков, труба не закроется

    // отправляет в канал id пользователя, для которого нужно создать тестовые данные
    // название метода должно совпадать с настройками definition и bindings в файлах .properties
    @Bean
    fun newUserActionProduce(): Supplier<Flux<Message<Long>>> {
        return Supplier { innerBus.asFlux() }
    }
}