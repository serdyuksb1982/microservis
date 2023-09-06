package ru.serdyuk.micro.planner.utils.webclient;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import ru.serdyuk.micro.planner.entity.User;

@Component
public class UserWebClientBuilder {

    private static final String baseUrl = "http://localhost:8765/planner-users/user/";

    private static final String baseUrlData = "http://localhost:8765/planner-todo/data/";

    //проверка, существует ли пользователь
    public boolean userExists(Long userId) {
        try {
            User user = WebClient.create(baseUrl)
                    .post()
                    .uri("id")
                    .bodyValue(userId)
                    .retrieve()
                    .bodyToFlux(User.class)
                    .blockFirst();// блокируется поток до получения 1-ой записи
            if (user != null) {
                return true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    //Async
    public Flux<User> userExistsAsync(Long userId) {
        Flux<User> fluxUser = WebClient.create(baseUrl)
                .post()
                .uri("id")
                .bodyValue(userId)
                .retrieve()
                .bodyToFlux(User.class);
        return fluxUser;
    }

    // init data loading
    public Flux<Boolean> initUserDataLoading(Long userId) {
        Flux<Boolean> fluxUser = WebClient.create(baseUrlData)
                .post()
                .uri("init")
                .bodyValue(userId)
                .retrieve()
                .bodyToFlux(Boolean.class);
        return fluxUser;
    }
}
