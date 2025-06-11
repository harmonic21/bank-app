package ru.yandex.practicum.transfer.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.web.context.annotation.RequestScope;
import ru.yandex.practicum.account.api.AccountApi;
import ru.yandex.practicum.account.api.UserApi;
import ru.yandex.practicum.blocker.ApiClient;
import ru.yandex.practicum.blocker.api.CheckApi;
import ru.yandex.practicum.exchange.api.CurrencyApi;
import ru.yandex.practicum.notification.api.NotificationApi;

@Configuration
public class RestClientConfiguration {

    @Bean
    @RequestScope
    public ApiClient blockerApiClient(OAuth2AuthorizedClientManager manager,
                                      @Value("${rest.client.blocker.url}") String blockerUrl) {
        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest.withClientRegistrationId("transfer-service")
                .principal("system")
                .build();
        ApiClient client = new ApiClient();
        client.setBasePath(blockerUrl);
        client.setBearerToken(manager.authorize(request).getAccessToken().getTokenValue());
        return client;
    }

    @Bean
    @RequestScope
    public ru.yandex.practicum.account.ApiClient accountsApiClient(OAuth2AuthorizedClientManager manager,
                                                                   @Value("${rest.client.accounts.url}") String accountsUrl) {
        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest.withClientRegistrationId("transfer-service")
                .principal("system")
                .build();
        ru.yandex.practicum.account.ApiClient client = new ru.yandex.practicum.account.ApiClient();
        client.setBasePath(accountsUrl);
        client.setBearerToken(manager.authorize(request).getAccessToken().getTokenValue());
        return client;
    }

    @Bean
    @RequestScope
    public ru.yandex.practicum.notification.ApiClient notificationApiClient(OAuth2AuthorizedClientManager manager,
                                                                            @Value("${rest.client.notification.url}") String notificationUrl) {
        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest.withClientRegistrationId("transfer-service")
                .principal("system")
                .build();
        ru.yandex.practicum.notification.ApiClient client = new ru.yandex.practicum.notification.ApiClient();
        client.setBasePath(notificationUrl);
        client.setBearerToken(manager.authorize(request).getAccessToken().getTokenValue());
        return client;
    }

    @Bean
    @RequestScope
    public ru.yandex.practicum.exchange.ApiClient exchangeApiClient(OAuth2AuthorizedClientManager manager,
                                                                        @Value("${rest.client.exchange.url}") String exchangeUrl) {
        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest.withClientRegistrationId("transfer-service")
                .principal("system")
                .build();
        ru.yandex.practicum.exchange.ApiClient client = new ru.yandex.practicum.exchange.ApiClient();
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
    public NotificationApi notificationApi(ru.yandex.practicum.notification.ApiClient notificationApiClient) {
        return new NotificationApi(notificationApiClient);
    }

    @Bean
    public CurrencyApi currencyApi(ru.yandex.practicum.exchange.ApiClient exchangeApiClient) {
        return new CurrencyApi(exchangeApiClient);
    }
}
