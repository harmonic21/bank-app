package ru.yandex.practicum.account.service;

import io.micrometer.tracing.Tracer;
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
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final Tracer tracer;

    @Transactional
    public void createNewAccount(String userName, CreateAccountRq request) {
        callDbAndTraceIt(() -> userRepository.findByUsername(userName), "user_repository.find_by_username")
                .ifPresent(user -> {
                    request.getCurrency().stream()
                            .filter(currency -> user.getAccounts().isEmpty() || user.getAccounts().stream().noneMatch(account -> Objects.equals(currency, account.getCurrency())))
                            .map(currency -> mapNewAccount(user, currency))
                            .forEach(user::addAccount);
                    callDbAndTraceIt(() -> userRepository.save(user), "userRepository.save");
                });
    }

    @Transactional
    public void deleteAccount(String userName, DeleteAccountRq request) {
        callDbAndTraceIt(() -> accountRepository.findAllUserAccounts(userName), "account_repository.find_all_user_accounts")
                        .forEach(account -> {
                            if (request.getCurrency().contains(account.getCurrency())) {
                                callDbAndTraceIt(() -> accountRepository.delete(account), "account_repository.delete");
                            }
                        });
    }

    @Transactional
    public void addCashToAccount(String userName, ChangeCashRq request) {
        callDbAndTraceIt(() -> accountRepository.findUserAccountWithCurrency(userName, request.getCurrency()), "account_repository.find_user_account_with_currency")
                .ifPresent(account -> {
                    account.setBalance(account.getBalance().add(request.getValue()));
                    callDbAndTraceIt(() -> accountRepository.save(account), "account_repository.save");
                });
    }

    @Transactional
    public void getCashFromAccount(String userName, ChangeCashRq request) {
        callDbAndTraceIt(() -> accountRepository.findUserAccountWithCurrency(userName, request.getCurrency()), "account_repository.find_user_account_with_currency")
                .ifPresent(account -> {
                    if (account.getBalance().compareTo(request.getValue()) < 0) {
                        throw new OperationBlockException("На счете %s недостаточно средств".formatted(request.getCurrency()));
                    }
                    account.setBalance(account.getBalance().subtract(request.getValue()));
                    callDbAndTraceIt(() -> accountRepository.save(account), "account_repository.save");
                });
    }

    @Transactional
    public void transferCash(String userName, TransferCashRq request) {
        callDbAndTraceIt(() -> accountRepository.findUserAccountWithCurrency(userName, request.getFromAccount()), "account_repository.find_user_account_with_currency")
                .ifPresentOrElse(
                        account -> {
                            if (account.getBalance().compareTo(request.getFromAccountValue()) < 0) {
                                throw new OperationBlockException("На счете %s недостаточно средств".formatted(request.getFromAccount()));
                            }
                            account.setBalance(account.getBalance().subtract(request.getFromAccountValue()));
                            callDbAndTraceIt(() -> accountRepository.save(account), "account_repository.save");
                        },
                        () -> {
                            throw new OperationBlockException("У пользователя %s нет счета %s".formatted(userName, request.getFromAccount()));
                        }
                );
        callDbAndTraceIt(() -> accountRepository.findUserAccountWithCurrency(userName, request.getToAccount()), "account_repository.find_user_account_with_currency")
                .ifPresentOrElse(
                        account -> {
                            account.setBalance(account.getBalance().add(request.getToAccountValue()));
                            callDbAndTraceIt(() -> accountRepository.save(account), "account_repository.save");
                        },
                        () -> {
                            throw new OperationBlockException("У пользователя %s нет счета %s".formatted(request.getToUser(), request.getToAccount()));
                        }
                );
    }

    private <T> T callDbAndTraceIt(Supplier<T> runnable, String traceName) {
        var span = tracer.nextSpan().remoteServiceName("postgre-db").name(traceName).start();
        try {
            return runnable.get();
        } finally {
            span.end();
        }
    }

    private void callDbAndTraceIt(Runnable runnable, String traceName) {
        var span = tracer.nextSpan().remoteServiceName("postgre-db").name(traceName).start();
        try {
            runnable.run();
        } finally {
            span.end();
        }
    }

    private UserAccountEntity mapNewAccount(UserEntity user, String currency) {
        var account = new UserAccountEntity();
        account.setUser(user);
        account.setBalance(BigDecimal.ZERO);
        account.setCurrency(currency);
        return account;
    }
}
