package ru.yandex.practicum.cash.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.account.api.AccountApi;
import ru.yandex.practicum.account.api.UserApi;
import ru.yandex.practicum.blocker.api.CheckApi;
import ru.yandex.practicum.notification.api.NotificationApi;

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

    @Bean
    public NotificationApi notificationApi() {
        return new NotificationApi();
    }

    @Bean
    public UserApi userApi() {
        return new UserApi();
    }
}
