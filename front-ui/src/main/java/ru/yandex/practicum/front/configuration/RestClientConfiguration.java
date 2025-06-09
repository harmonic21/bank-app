package ru.yandex.practicum.front.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.web.context.annotation.RequestScope;
import ru.yandex.practicum.account.ApiClient;
import ru.yandex.practicum.account.api.AccountApi;
import ru.yandex.practicum.account.api.UserApi;
import ru.yandex.practicum.cash.api.CashApi;
import ru.yandex.practicum.exchange.api.CurrencyApi;

@Configuration
public class RestClientConfiguration {

    @Bean
    @RequestScope
    public ApiClient accountsApiClient(OAuth2AuthorizedClientManager manager) {
        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest.withClientRegistrationId("front-ui")
                .principal("system")
                .build();
        ApiClient client = new ApiClient();
        String tokenValue = manager.authorize(request).getAccessToken().getTokenValue();
        System.out.println(tokenValue);
        client.setBearerToken(tokenValue);
        return client;
    }

    @Bean
    public UserApi userServiceClient(ApiClient accountsApiClient) {
        return new UserApi(accountsApiClient);
    }

    @Bean
    public AccountApi userAccountServiceClient(ApiClient accountsApiClient) {
        return new AccountApi(accountsApiClient);
    }

    @Bean
    public CashApi cashClient() {
        return new CashApi();
    }

    @Bean
    public CurrencyApi currencyApi() {
        return new CurrencyApi();
    }
}
