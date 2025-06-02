package ru.yandex.practicum.exchange.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.exchange.api.CurrencyApi;
import ru.yandex.practicum.exchange.model.CurrencyInfoRs;
import ru.yandex.practicum.exchange.model.ResponseInfo;
import ru.yandex.practicum.exchange.model.UpdateCurrencyRq;
import ru.yandex.practicum.exchange.service.CurrencyInfoStorage;

@RestController
@RequiredArgsConstructor
public class CurrencyController implements CurrencyApi {

    private final CurrencyInfoStorage currencyInfoStorage;

    @Override
    public ResponseEntity<CurrencyInfoRs> actualCurrencyInfo() {
        var currencyInfo = currencyInfoStorage.getCurrencyInfo();
        return ResponseEntity.ok(new CurrencyInfoRs(new ResponseInfo(true)).currencyInfo(currencyInfo));
    }

    @Override
    public ResponseEntity<Void> updateCurrencyInfo(UpdateCurrencyRq updateCurrencyRq) {
        currencyInfoStorage.updateCurrencyInfo(updateCurrencyRq.getCurrencyInfo());
        return ResponseEntity.ok().build();
    }
}
