package ru.yandex.practicum.cash.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.cash.api.CashApi;
import ru.yandex.practicum.cash.model.ChangeCashRq;
import ru.yandex.practicum.cash.model.ResponseInfo;
import ru.yandex.practicum.cash.service.CashService;

@RestController
@RequiredArgsConstructor
public class CashController implements CashApi {

    private final CashService cashService;

    @Override
    public ResponseEntity<ResponseInfo> updateCash(ChangeCashRq changeCashRq) {
        cashService.updateCash(changeCashRq);
        return ResponseEntity.ok(new ResponseInfo(true));
    }
}
