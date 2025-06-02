package ru.yandex.practicum.exchange.generator.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.exchange.api.CurrencyApi;

@Configuration
public class RestClientConfiguration {

    @Bean
    public CurrencyApi currencyApi() {
        return new CurrencyApi();
    }
}
