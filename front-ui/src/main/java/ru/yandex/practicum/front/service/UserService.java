package ru.yandex.practicum.front.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.yandex.practicum.account.api.UserApi;
import ru.yandex.practicum.account.model.UserInfoRs;
import ru.yandex.practicum.front.error.IntegrationErrorException;
import ru.yandex.practicum.front.error.UserNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserApi userServiceClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userServiceClient.getUserInfoByUsername(username)
                .onErrorMap(throwable -> mapAccountServiceError(throwable, username))
                .blockOptional()
                .map(UserInfoRs::getUserInfo)
                .map(userInfo -> User.builder()
                        .username(userInfo.getUsername())
                        .password(userInfo.getPassword())
                        .roles(userInfo.getRoles().toArray(new String[0]))
                        .build()
                )
                .orElseThrow(null);
    }

    private Throwable mapAccountServiceError(Throwable throwable, String userName) {
        if (throwable instanceof WebClientResponseException.NotFound e) {
            return new UserNotFoundException(userName);
        } else {
            return new IntegrationErrorException("account service");
        }
    }
}
