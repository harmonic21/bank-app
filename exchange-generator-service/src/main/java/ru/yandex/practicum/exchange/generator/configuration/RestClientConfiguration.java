package ru.yandex.practicum.exchange.generator.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.exchange.api.CurrencyApi;

@Configuration
public class RestClientConfiguration {

    @Bean
    public CurrencyApi currencyApi(@Value("${rest.client.exchange.url}") String exchangeUrl) {
        var client = new CurrencyApi();
        client.getApiClient().setBasePath(exchangeUrl);
        return client;
    }
}
