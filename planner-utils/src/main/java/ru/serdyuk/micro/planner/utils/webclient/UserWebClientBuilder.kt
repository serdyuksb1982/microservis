package ru.serdyuk.micro.planner.utils.webclient

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import ru.serdyuk.micro.planner.entity.User

@Component
class UserWebClientBuilder {

    companion object {
        private const val baseUrl = "http://localhost:8765/planner-users/user/"
        private const val baseUrlData = "http://localhost:8765/planner-todo/data/"
    }

    //проверка, существует ли пользователь
    fun userExists(userId: Long): Boolean {
        try {
            val user = WebClient.create(baseUrl)
                .post()
                .uri("id")
                .bodyValue(userId)
                .retrieve()
                .bodyToFlux(User::class.java)
                .blockFirst() // блокируется поток до получения 1-ой записи
            if (user != null) {
                return true
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        return false
    }

    //Async
    fun userExistsAsync(userId: Long): Flux<User> {
        return WebClient.create(baseUrl)
            .post()
            .uri("id")
            .bodyValue(userId)
            .retrieve()
            .bodyToFlux(User::class.java)
    }

    // init data loading
    fun initUserDataLoading(userId: Long): Flux<Boolean> {
        return WebClient.create(baseUrlData)
            .post()
            .uri("init")
            .bodyValue(userId)
            .retrieve()
            .bodyToFlux(Boolean::class.java)
    }


}