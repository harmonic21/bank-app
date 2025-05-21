package ru.yandex.practicum.front.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.account.api.AccountApi;
import ru.yandex.practicum.account.api.UserApi;
import ru.yandex.practicum.account.model.CreateAccountRq;
import ru.yandex.practicum.account.model.DeleteAccountRq;
import ru.yandex.practicum.account.model.UpdateUserInfoRq;
import ru.yandex.practicum.account.model.UserInfoRs;
import ru.yandex.practicum.front.constants.AccountCurrency;
import ru.yandex.practicum.front.dto.AccountInfoDto;
import ru.yandex.practicum.front.dto.PersonalUserInfoDto;

import java.util.List;
import java.util.Optional;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
public class UserAccountService {
    private final UserApi userServiceClient;
    private final AccountApi userAccountServiceClient;
    private final PasswordEncoder passwordEncoder;

    public Optional<UserInfoRs> getUserInfoDetailByUserName(String username) {
        return userServiceClient.getUserInfoByUsername(username).blockOptional();
    }

    public void updateUserPassword(String userName, String password) {
        userServiceClient.updateUserInfo(userName, new UpdateUserInfoRq().password(passwordEncoder.encode(password))).block();
    }

    public void updatePersonalUserInfo(String userName, PersonalUserInfoDto personalInfo) {
        var personalInfoUpdateResult = userServiceClient.updateUserInfo(
                        userName,
                        new UpdateUserInfoRq().fullName(personalInfo.getName()).birthDay(personalInfo.getBirthDate())
                )
                .blockOptional()
                .orElse(null);
        createOrDeleteAccounts(userName, personalInfo.getAccounts());
    }

    private void createOrDeleteAccounts(String userName, List<AccountInfoDto> accounts) {
        var accountsToCreate = accounts.stream()
                .filter(AccountInfoDto::isExists)
                .map(AccountInfoDto::getCurrency)
                .map(AccountCurrency::name)
                .toList();
        var accountsToDelete = accounts.stream()
                .filter(not(AccountInfoDto::isExists))
                .map(AccountInfoDto::getCurrency)
                .map(AccountCurrency::name)
                .toList();
        Mono.zip(
                userAccountServiceClient.createUserAccount(userName, new CreateAccountRq().currency(accountsToCreate)),
                userAccountServiceClient.deleteUserAccount(userName, new DeleteAccountRq().currency(accountsToDelete))
        ).block();
    }
}
