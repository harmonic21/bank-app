package ru.yandex.practicum.exchange.generator.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import ru.yandex.practicum.exchange.ApiClient;
import ru.yandex.practicum.exchange.api.CurrencyApi;

@Configuration
public class RestClientConfiguration {

    @Bean
    @Scope("prototype")
    public ApiClient exchangeApiClient(OAuth2AuthorizedClientManager manager,
                                       @Value("${rest.client.exchange.url}") String accountsUrl) {
        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest.withClientRegistrationId("exchange-generator-service")
                .principal("system")
                .build();
        ApiClient client = new ApiClient();
        client.setBasePath(accountsUrl);
        client.setBearerToken(manager.authorize(request).getAccessToken().getTokenValue());
        return client;
    }

    @Bean
    @Scope("prototype")
    public CurrencyApi currencyApi(ApiClient exchangeApiClient) {
        return new CurrencyApi(exchangeApiClient);
    }
}
