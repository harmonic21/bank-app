package ru.yandex.practicum.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.account.entity.UserAccountEntity;
import ru.yandex.practicum.account.entity.UserEntity;
import ru.yandex.practicum.account.model.ChangeCashRq;
import ru.yandex.practicum.account.model.CreateAccountRq;
import ru.yandex.practicum.account.model.DeleteAccountRq;
import ru.yandex.practicum.account.repository.AccountRepository;
import ru.yandex.practicum.account.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public void createNewAccount(String userName, CreateAccountRq request) {
        userRepository.findByUsername(userName)
                .ifPresent(user -> {
                    request.getCurrency().stream()
                            .filter(currency -> user.getAccounts().isEmpty() || user.getAccounts().stream().noneMatch(account -> Objects.equals(currency, account.getCurrency())))
                            .map(currency -> mapNewAccount(user, currency))
                            .forEach(user::addAccount);
                    userRepository.save(user);
                });
    }

    @Transactional
    public void deleteAccount(String userName, DeleteAccountRq request) {
        accountRepository.findAllUserAccounts(userName)
                        .forEach(account -> {
                            if (request.getCurrency().contains(account.getCurrency())) {
                                accountRepository.delete(account);
                            }
                        });
    }

    @Transactional
    public void addCashToAccount(String userName, ChangeCashRq request) {
        accountRepository.findUserAccountWithCurrency(userName, request.getCurrency())
                .ifPresent(account -> {
                    account.setBalance(account.getBalance().add(request.getValue()));
                    accountRepository.save(account);
                });
    }

    @Transactional
    public void getCashFromAccount(String userName, ChangeCashRq request) {
        accountRepository.findUserAccountWithCurrency(userName, request.getCurrency())
                .ifPresent(account -> {
                    account.setBalance(account.getBalance().subtract(request.getValue()));
                    accountRepository.save(account);
                });
    }

    private UserAccountEntity mapNewAccount(UserEntity user, String currency) {
        var account = new UserAccountEntity();
        account.setUser(user);
        account.setBalance(BigDecimal.ZERO);
        account.setCurrency(currency);
        return account;
    }
}
