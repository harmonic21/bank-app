package ru.yandex.practicum.front.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.account.api.UserApi;
import ru.yandex.practicum.account.model.UserInfoRs;
import ru.yandex.practicum.account.model.UserRegisterInfo;
import ru.yandex.practicum.front.dto.ErrorStorage;
import ru.yandex.practicum.front.dto.SignupUserInfoDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SignupUserService {

    private final UserApi userServiceClient;
    private final PasswordEncoder passwordEncoder;

    public ErrorStorage signupNewUser(SignupUserInfoDto userInfo) {
        ErrorStorage errorStorage = new ErrorStorage();
        UserRegisterInfo userRegisterInfo = new UserRegisterInfo()
                .username(userInfo.getLogin())
                .password(passwordEncoder.encode(userInfo.getPasswordInfo().getPassword()))
                .fullName(userInfo.getPersonalInfo().getName())
                .birthDay(userInfo.getPersonalInfo().getBirthDate())
                .email(userInfo.getPersonalInfo().getEmail())
                .roles(List.of("USER"));
        UserInfoRs response = userServiceClient.registerNewUser(userRegisterInfo)
                .onErrorResume(t -> t instanceof WebClientResponseException, t -> Mono.just(((WebClientResponseException) t).getResponseBodyAs(UserInfoRs.class)))
                .block();

        if (BooleanUtils.isFalse(response.getResponseInfo().getStatus())) {
            errorStorage.addError(response.getResponseInfo().getError().getErrorMessage());
        }
        return errorStorage;
    }
}
