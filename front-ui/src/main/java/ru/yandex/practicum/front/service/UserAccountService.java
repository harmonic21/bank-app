package ru.yandex.practicum.front.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.account.api.UserApi;
import ru.yandex.practicum.account.model.UpdateUserInfoRq;
import ru.yandex.practicum.account.model.UserInfoRs;
import ru.yandex.practicum.front.dto.PersonalUserInfoDto;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAccountService {
    private final UserApi userAccountServiceClient;
    private final PasswordEncoder passwordEncoder;

    public Optional<UserInfoRs> getUserInfoDetailByUserName(String username) {
        return userAccountServiceClient.getUserInfoByUsername(username).blockOptional();
    }

    public void updateUserPassword(String userName, String password) {
        userAccountServiceClient.updateUserInfo(userName, new UpdateUserInfoRq().password(passwordEncoder.encode(password))).block();
    }

    public UserInfoRs updatePersonalUserInfo(String userName, PersonalUserInfoDto personalInfo) {
        return userAccountServiceClient.updateUserInfo(
                        userName,
                        new UpdateUserInfoRq().fullName(personalInfo.getName()).birthDay(personalInfo.getBirthDate())
                )
                .blockOptional()
                .orElse(null);
    }
}
