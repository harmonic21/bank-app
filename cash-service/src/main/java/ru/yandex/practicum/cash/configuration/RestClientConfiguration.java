package ru.yandex.practicum.cash.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.account.api.AccountApi;
import ru.yandex.practicum.blocker.api.CheckApi;

@Configuration
public class RestClientConfiguration {

    @Bean
    public CheckApi checkApi() {
        return new CheckApi();
    }

    @Bean
    public AccountApi accountApi() {
        return new AccountApi();
    }
}
