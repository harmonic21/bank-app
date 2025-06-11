package ru.yandex.practicum.front.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.account.api.AccountApi;
import ru.yandex.practicum.account.api.UserApi;
import ru.yandex.practicum.account.model.*;
import ru.yandex.practicum.cash.api.CashApi;
import ru.yandex.practicum.cash.model.ResponseInfo;
import ru.yandex.practicum.front.constants.AccountCurrency;
import ru.yandex.practicum.front.dto.AccountInfoDto;
import ru.yandex.practicum.front.dto.PersonalUserInfoDto;
import ru.yandex.practicum.front.dto.ShortUserInfoDto;
import ru.yandex.practicum.front.util.RestClientUtility;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
public class UserAccountService {
    private final UserApi userServiceClient;
    private final AccountApi userAccountServiceClient;
    private final CashApi cashClient;
    private final PasswordEncoder passwordEncoder;

    public Optional<UserInfoRs> getUserInfoDetailByUserName(String username) {
        return userServiceClient.getUserInfoByUsername(username).blockOptional();
    }

    public void updateUserPassword(String userName, String password) {
        userServiceClient.updateUserInfo(userName, new UpdateUserInfoRq().password(passwordEncoder.encode(password))).block();
    }

    public List<ShortUserInfoDto> getAllRegisteredUsers(String currentUser) {
        return userServiceClient.getAllRegisteredUsers()
                .flatMapIterable(RegisteredUsersRs::getUsers)
                .filter(user -> ObjectUtils.notEqual(user.getUsername(), currentUser))
                .map(user -> new ShortUserInfoDto().setLogin(user.getUsername()).setName(user.getFullName()))
                .collectList()
                .block();
    }

    public void updatePersonalUserInfo(String userName, PersonalUserInfoDto personalInfo) {
        var personalInfoUpdateResult = userServiceClient.updateUserInfo(
                        userName,
                        new UpdateUserInfoRq()
                                .fullName(personalInfo.getName())
                                .birthDay(personalInfo.getBirthDate())
                                .email(personalInfo.getEmail())
                )
                .blockOptional()
                .orElse(null);
        createOrDeleteAccounts(userName, personalInfo.getAccounts());
    }

    public ResponseInfo updateCash(String action, String userName, String currency, BigDecimal value) {
        return cashClient.updateCash(
                        new ru.yandex.practicum.cash.model.ChangeCashRq()
                                .userName(userName)
                                .action(action)
                                .currency(currency)
                                .value(value)
                )
                .onErrorResume(
                        RestClientUtility::isWebClientResponseException,
                        t -> Mono.just(RestClientUtility.getResponseBodyFromError(t, ResponseInfo.class))
                )
                .block();
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
