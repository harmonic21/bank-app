package ru.yandex.practicum.front.configuration;

import org.springframework.beans.factory.annotation.Value;
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
import ru.yandex.practicum.transfer.api.TransferApi;

@Configuration
public class RestClientConfiguration {

    @Bean
    @RequestScope
    public ApiClient accountsApiClient(OAuth2AuthorizedClientManager manager,
                                       @Value("${rest.client.accounts.url}") String accountsUrl) {
        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest.withClientRegistrationId("front-ui")
                .principal("system")
                .build();
        ApiClient client = new ApiClient();
        client.setBasePath(accountsUrl);
        client.setBearerToken(manager.authorize(request).getAccessToken().getTokenValue());
        return client;
    }

    @Bean
    @RequestScope
    public ru.yandex.practicum.cash.ApiClient cashApiClient(OAuth2AuthorizedClientManager manager,
                                                            @Value("${rest.client.cash.url}") String cashUrl) {
        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest.withClientRegistrationId("front-ui")
                .principal("system")
                .build();
        ru.yandex.practicum.cash.ApiClient client = new ru.yandex.practicum.cash.ApiClient();
        client.setBasePath(cashUrl);
        client.setBearerToken(manager.authorize(request).getAccessToken().getTokenValue());
        return client;
    }

    @Bean
    @RequestScope
    public ru.yandex.practicum.exchange.ApiClient exchangeApiClient(OAuth2AuthorizedClientManager manager,
                                                                    @Value("${rest.client.exchange.url}") String exchangeUrl) {
        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest.withClientRegistrationId("front-ui")
                .principal("system")
                .build();
        ru.yandex.practicum.exchange.ApiClient client = new ru.yandex.practicum.exchange.ApiClient();
        client.setBasePath(exchangeUrl);
        client.setBearerToken(manager.authorize(request).getAccessToken().getTokenValue());
        return client;
    }

    @Bean
    @RequestScope
    public ru.yandex.practicum.transfer.ApiClient transferApiClient(OAuth2AuthorizedClientManager manager,
                                                                    @Value("${rest.client.transfer.url}") String transferUrl) {
        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest.withClientRegistrationId("front-ui")
                .principal("system")
                .build();
        ru.yandex.practicum.transfer.ApiClient client = new ru.yandex.practicum.transfer.ApiClient();
        client.setBasePath(transferUrl);
        client.setBearerToken(manager.authorize(request).getAccessToken().getTokenValue());
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
    public CashApi cashClient(ru.yandex.practicum.cash.ApiClient cashApiClient) {
        return new CashApi(cashApiClient);
    }

    @Bean
    public CurrencyApi currencyApi(ru.yandex.practicum.exchange.ApiClient exchangeApiClient) {
        return new CurrencyApi(exchangeApiClient);
    }

    @Bean
    public TransferApi transferApi(ru.yandex.practicum.transfer.ApiClient transferApiClient) {
        return new TransferApi(transferApiClient);
    }
}
