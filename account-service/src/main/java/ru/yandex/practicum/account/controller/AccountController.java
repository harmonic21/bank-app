package ru.yandex.practicum.account.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.account.api.AccountApi;
import ru.yandex.practicum.account.model.*;
import ru.yandex.practicum.account.service.AccountService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountController implements AccountApi {

    private final AccountService accountService;

    @Override
    public ResponseEntity<Void> createUserAccount(String username, CreateAccountRq createAccountRq) {
        log.info("Получен запрос на создание нового пользователя с логином {}", username);
        accountService.createNewAccount(username, createAccountRq);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteUserAccount(String username, DeleteAccountRq deleteAccountRq) {
        log.info("Получен запрос на удаление пользователя с логином {}", username);
        accountService.deleteAccount(username, deleteAccountRq);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> addCashToAccount(String username, ChangeCashRq changeCashRq) {
        log.info("Получен запрос на зачисление наличных средств для пользователя с логином {}", username);
        accountService.addCashToAccount(username, changeCashRq);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ResponseInfo> getCashFromAccount(String username, ChangeCashRq changeCashRq) {
        log.info("Получен запрос на снятие наличных средств для пользователя с логином {}", username);
        accountService.getCashFromAccount(username, changeCashRq);
        return ResponseEntity.ok(new ResponseInfo(true));
    }

    @Override
    public ResponseEntity<ResponseInfo> transferCash(String username, TransferCashRq transferCashRq) {
        log.info("Получен запрос на перевод средств от пользователя с логином {}", username);
        accountService.transferCash(username, transferCashRq);
        return ResponseEntity.ok(new ResponseInfo(true));
    }
}
