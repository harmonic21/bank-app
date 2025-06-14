package ru.yandex.practicum.exchange.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
                        .requestMatchers(HttpMethod.GET, "/currency/info/actual").hasAuthority("READ_CURRENCY_VALUE")
                        .requestMatchers(HttpMethod.PUT, "/currency/info/update").hasAuthority("CHANGE_CURRENCY_VALUE")
                        .requestMatchers(HttpMethod.POST, "/currency/exchange").hasAuthority("EXCHANGE_CURRENCY")
                        .requestMatchers("/actuator/**").permitAll()
                )
                .oauth2ResourceServer(customizer -> customizer
                        .jwt(jwtCustomizer -> {
                            JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
                            jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> Optional.ofNullable(jwt.getClaim("resource_access"))
                                    .map(access -> ((Map<String, Object>)access).get("exchange-service"))
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
}
