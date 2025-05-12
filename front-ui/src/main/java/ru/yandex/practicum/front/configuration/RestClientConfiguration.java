package ru.yandex.practicum.front.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.account.api.UserApi;

@Configuration
public class RestClientConfiguration {

    @Bean
    public UserApi userAccountServiceClient() {
        return new UserApi();
    }
}
