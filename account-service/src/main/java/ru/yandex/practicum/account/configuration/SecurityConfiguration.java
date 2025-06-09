package ru.yandex.practicum.account.configuration;

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
                        .requestMatchers(HttpMethod.GET, "/user/**").hasAuthority("READ_USER_INFO")
                        .requestMatchers(HttpMethod.PUT, "/user/**").hasAuthority("CHANGE_USER_INFO")
                        .requestMatchers(HttpMethod.POST, "/user/**").hasAuthority("CHANGE_USER_INFO")
                        .requestMatchers(HttpMethod.GET, "/account/**").hasAuthority("READ_ACCOUNTS_INFO")
                        .requestMatchers(HttpMethod.PUT, "/account/**").hasAuthority("CHANGE_ACCOUNTS_INFO")
                        .requestMatchers(HttpMethod.POST, "/account/**").hasAuthority("CHANGE_ACCOUNTS_INFO")
                        .requestMatchers(HttpMethod.DELETE, "/account/**").hasAuthority("CHANGE_ACCOUNTS_INFO")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(customizer -> customizer
                        .jwt(jwtCustomizer -> {
                            JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
                            jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> Optional.ofNullable(jwt.getClaim("resource_access"))
                                        .map(access -> ((Map<String, Object>)access).get("accounts-service"))
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
