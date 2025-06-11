package ru.yandex.practicum.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.account.entity.UserAccountEntity;
import ru.yandex.practicum.account.entity.UserEntity;
import ru.yandex.practicum.account.exceptions.OperationBlockException;
import ru.yandex.practicum.account.model.ChangeCashRq;
import ru.yandex.practicum.account.model.CreateAccountRq;
import ru.yandex.practicum.account.model.DeleteAccountRq;
import ru.yandex.practicum.account.model.TransferCashRq;
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
                    if (account.getBalance().compareTo(request.getValue()) < 0) {
                        throw new OperationBlockException("На счете %s недостаточно средств".formatted(request.getCurrency()));
                    }
                    account.setBalance(account.getBalance().subtract(request.getValue()));
                    accountRepository.save(account);
                });
    }

    @Transactional
    public void transferCash(String userName, TransferCashRq request) {
        accountRepository.findUserAccountWithCurrency(userName, request.getFromAccount())
                .ifPresentOrElse(
                        account -> {
                            if (account.getBalance().compareTo(request.getFromAccountValue()) < 0) {
                                throw new OperationBlockException("На счете %s недостаточно средств".formatted(request.getFromAccount()));
                            }
                            account.setBalance(account.getBalance().subtract(request.getFromAccountValue()));
                            accountRepository.save(account);
                        },
                        () -> {
                            throw new OperationBlockException("У пользователя %s нет счета %s".formatted(userName, request.getFromAccount()));
                        }
                );
        accountRepository.findUserAccountWithCurrency(request.getToUser(), request.getToAccount())
                .ifPresentOrElse(
                        account -> {
                            account.setBalance(account.getBalance().add(request.getToAccountValue()));
                            accountRepository.save(account);
                        },
                        () -> {
                            throw new OperationBlockException("У пользователя %s нет счета %s".formatted(request.getToUser(), request.getToAccount()));
                        }
                );
    }

    private UserAccountEntity mapNewAccount(UserEntity user, String currency) {
        var account = new UserAccountEntity();
        account.setUser(user);
        account.setBalance(BigDecimal.ZERO);
        account.setCurrency(currency);
        return account;
    }
}
