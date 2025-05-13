package ru.yandex.practicum.front.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.account.api.UserApi;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserApi userAccountServiceClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userAccountServiceClient.getUserInfoByUsername(username)
                .blockOptional()
                .map(userInfo -> User.builder()
                        .username(userInfo.getUsername())
                        .password(userInfo.getPassword())
                        .roles(userInfo.getRoles().toArray(new String[0]))
                        .build()
                )
                .orElse(null);
    }
}
