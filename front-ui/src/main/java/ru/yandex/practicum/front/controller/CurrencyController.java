package ru.yandex.practicum.front.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.exchange.api.CurrencyApi;
import ru.yandex.practicum.exchange.model.CurrencyInfoRs;
import ru.yandex.practicum.front.dto.CurrencyRateDto;
import ru.yandex.practicum.front.error.IntegrationErrorException;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyApi currencyApi;
    private final MeterRegistry meterRegistry;

    @GetMapping("/api/rates")
    public List<CurrencyRateDto> getRates() {
        return currencyApi.actualCurrencyInfo()
                .map(CurrencyInfoRs::getCurrencyInfo)
                .map(currency -> currency.stream().map(actual -> new CurrencyRateDto().setTitle(actual.getName()).setName(actual.getShortName()).setValue(actual.getValue().doubleValue())).toList())
                .doOnError(ignore -> Counter.builder("currency_rate_error").register(meterRegistry).increment())
                .onErrorMap(t -> new IntegrationErrorException("exchange-service"))
                .block();
    }
}
