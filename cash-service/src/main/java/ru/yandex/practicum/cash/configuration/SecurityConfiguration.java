package ru.yandex.practicum.cash.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        return security
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.POST, "/cash/update").hasAuthority("CHANGE_CASH")
                        .requestMatchers("/actuator/**").permitAll()
                )
                .oauth2ResourceServer(customizer -> customizer
                        .jwt(jwtCustomizer -> {
                            JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
                            jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> Optional.ofNullable(jwt.getClaim("resource_access"))
                                    .map(access -> ((Map<String, Object>)access).get("cash-service"))
                                    .map(accountsAccess -> ((Map<String, Object>)accountsAccess).get("roles"))
                                    .map(roles -> (List<String>) roles)
                                    .stream()
                                    .flatMap(Collection::stream)
                                    .map(SimpleGrantedAuthority::new)
                                    .map(GrantedAuthority.class::cast)
                                    .toList());

                            jwtCustomizer.jwtAuthenticationConverter(jwtAuthenticationConverter);
                        })
                )
                .build();
    }

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(ClientRegistrationRepository clientRegistrationRepository,
                                                                 OAuth2AuthorizedClientService authorizedClientService) {
        AuthorizedClientServiceOAuth2AuthorizedClientManager manager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService);

        manager.setAuthorizedClientProvider(OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .refreshToken()
                .build());

        return manager;
    }
}
