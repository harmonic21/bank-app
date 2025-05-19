package ru.yandex.practicum.front.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.account.api.UserApi;
import ru.yandex.practicum.account.model.UserRegisterInfo;
import ru.yandex.practicum.front.dto.SignupUserInfoDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SignupUserService {

    private final UserApi userAccountServiceClient;
    private final PasswordEncoder passwordEncoder;

    public void signupNewUser(SignupUserInfoDto userInfo) {
        UserRegisterInfo userRegisterInfo = new UserRegisterInfo()
                .username(userInfo.getLogin())
                .password(passwordEncoder.encode(userInfo.getPasswordInfo().getPassword()))
                .fullName(userInfo.getPersonalInfo().getName())
                .birthDay(userInfo.getPersonalInfo().getBirthDate())
                .roles(List.of("USER"));
        userAccountServiceClient.registerNewUserWithHttpInfo(userRegisterInfo).block();
    }
}
