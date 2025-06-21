package ru.yandex.practicum.transfer.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.reactive.function.client.WebClient;
import ru.yandex.practicum.account.api.AccountApi;
import ru.yandex.practicum.account.api.UserApi;
import ru.yandex.practicum.blocker.ApiClient;
import ru.yandex.practicum.blocker.api.CheckApi;
import ru.yandex.practicum.exchange.api.CurrencyApi;

@Configuration
public class RestClientConfiguration {

    @Bean
    public WebClient blockerWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.build();
    }

    @Bean
    @RequestScope
    public ApiClient blockerApiClient(OAuth2AuthorizedClientManager manager,
                                      WebClient blockerWebClient,
                                      @Value("${rest.client.blocker.url}") String blockerUrl) {
        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest.withClientRegistrationId("transfer-service")
                .principal("system")
                .build();
        ApiClient client = new ApiClient(blockerWebClient);
        client.setBasePath(blockerUrl);
        client.setBearerToken(manager.authorize(request).getAccessToken().getTokenValue());
        return client;
    }

    @Bean
    public WebClient accountsWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.build();
    }

    @Bean
    @RequestScope
    public ru.yandex.practicum.account.ApiClient accountsApiClient(OAuth2AuthorizedClientManager manager,
                                                                   WebClient accountsWebClient,
                                                                   @Value("${rest.client.accounts.url}") String accountsUrl) {
        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest.withClientRegistrationId("transfer-service")
                .principal("system")
                .build();
        ru.yandex.practicum.account.ApiClient client = new ru.yandex.practicum.account.ApiClient(accountsWebClient);
        client.setBasePath(accountsUrl);
        client.setBearerToken(manager.authorize(request).getAccessToken().getTokenValue());
        return client;
    }

    @Bean
    public WebClient exchangeWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.build();
    }

    @Bean
    @RequestScope
    public ru.yandex.practicum.exchange.ApiClient exchangeApiClient(OAuth2AuthorizedClientManager manager,
                                                                    WebClient exchangeWebClient,
                                                                    @Value("${rest.client.exchange.url}") String exchangeUrl) {
        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest.withClientRegistrationId("transfer-service")
                .principal("system")
                .build();
        ru.yandex.practicum.exchange.ApiClient client = new ru.yandex.practicum.exchange.ApiClient(exchangeWebClient);
        client.setBasePath(exchangeUrl);
        client.setBearerToken(manager.authorize(request).getAccessToken().getTokenValue());
        return client;
    }

    @Bean
    public CheckApi checkApi(ApiClient blockerApiClient) {
        return new CheckApi(blockerApiClient);
    }

    @Bean
    public AccountApi accountApi(ru.yandex.practicum.account.ApiClient accountsApiClient) {
        return new AccountApi(accountsApiClient);
    }

    @Bean
    public UserApi userApi(ru.yandex.practicum.account.ApiClient accountsApiClient) {
        return new UserApi(accountsApiClient);
    }

    @Bean
    public CurrencyApi currencyApi(ru.yandex.practicum.exchange.ApiClient exchangeApiClient) {
        return new CurrencyApi(exchangeApiClient);
    }
}
