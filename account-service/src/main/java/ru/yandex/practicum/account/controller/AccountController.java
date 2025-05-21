package ru.yandex.practicum.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.account.api.AccountApi;
import ru.yandex.practicum.account.model.ChangeCashRq;
import ru.yandex.practicum.account.model.CreateAccountRq;
import ru.yandex.practicum.account.model.DeleteAccountRq;
import ru.yandex.practicum.account.service.AccountService;

@RestController
@RequiredArgsConstructor
public class AccountController implements AccountApi {

    private final AccountService accountService;

    @Override
    public ResponseEntity<Void> createUserAccount(String username, CreateAccountRq createAccountRq) {
        accountService.createNewAccount(username, createAccountRq);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteUserAccount(String username, DeleteAccountRq deleteAccountRq) {
        accountService.deleteAccount(username, deleteAccountRq);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> addCashToAccount(String username, ChangeCashRq changeCashRq) {
        accountService.addCashToAccount(username, changeCashRq);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> getCashFromAccount(String username, ChangeCashRq changeCashRq) {
        accountService.getCashFromAccount(username, changeCashRq);
        return ResponseEntity.ok().build();
    }
}
