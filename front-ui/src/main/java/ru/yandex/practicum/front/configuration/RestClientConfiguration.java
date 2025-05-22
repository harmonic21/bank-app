package ru.yandex.practicum.front.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.account.api.AccountApi;
import ru.yandex.practicum.account.api.UserApi;
import ru.yandex.practicum.cash.api.CashApi;

@Configuration
public class RestClientConfiguration {

    @Bean
    public UserApi userServiceClient() {
        return new UserApi();
    }

    @Bean
    public AccountApi userAccountServiceClient() {
        return new AccountApi();
    }

    @Bean
    public CashApi cashClient() {
        return new CashApi();
    }
}
