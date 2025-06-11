package ru.yandex.practicum.exchange.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.exchange.api.CurrencyApi;
import ru.yandex.practicum.exchange.model.*;
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

    @Override
    public ResponseEntity<ExchangeCurrencyRs> exchangeCurrency(ExchangeCurrencyRq exchangeCurrencyRq) {
        return ResponseEntity.ok(
                new ExchangeCurrencyRs(
                        exchangeCurrencyRq.getToCurrency(),
                        currencyInfoStorage.exchangeCurrency(
                                exchangeCurrencyRq.getFromCurrency(),
                                exchangeCurrencyRq.getToCurrency(),
                                exchangeCurrencyRq.getFromValue()
                        )
                )
        );
    }
}
