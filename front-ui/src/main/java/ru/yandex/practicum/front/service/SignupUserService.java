package ru.yandex.practicum.front.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.account.api.UserApi;
import ru.yandex.practicum.account.model.UserInfoRs;
import ru.yandex.practicum.account.model.UserRegisterInfo;
import ru.yandex.practicum.front.dto.ErrorStorage;
import ru.yandex.practicum.front.dto.SignupUserInfoDto;
import ru.yandex.practicum.front.util.RestClientUtility;

import java.util.List;
import java.util.Objects;

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
                .onErrorResume(
                        RestClientUtility::isWebClientResponseException,
                        t -> Mono.just(RestClientUtility.getResponseBodyFromError(t, UserInfoRs.class))
                )
                .block();

        if (Objects.isNull(response)) {
            errorStorage.addError(RestClientUtility.ACCOUNT_SERVICE_NOT_AVAILABLE_ERROR);
        } else if (BooleanUtils.isFalse(response.getResponseInfo().getStatus()) && Objects.nonNull(response.getResponseInfo().getError())) {
            errorStorage.addError(response.getResponseInfo().getError().getErrorMessage());
        }
        return errorStorage;
    }
}
