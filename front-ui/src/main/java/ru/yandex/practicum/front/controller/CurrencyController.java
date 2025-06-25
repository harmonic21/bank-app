package ru.yandex.practicum.front.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.exchange.api.CurrencyApi;
import ru.yandex.practicum.exchange.model.CurrencyInfoRs;
import ru.yandex.practicum.front.dto.CurrencyRateDto;
import ru.yandex.practicum.front.error.IntegrationErrorException;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyApi currencyApi;
    private final MeterRegistry meterRegistry;

    @GetMapping("/api/rates")
    public List<CurrencyRateDto> getRates() {
        log.info("Получен запрос на обновление курса валют");
        return currencyApi.actualCurrencyInfo()
                .map(CurrencyInfoRs::getCurrencyInfo)
                .map(currency -> currency.stream().map(actual -> new CurrencyRateDto().setTitle(actual.getName()).setName(actual.getShortName()).setValue(actual.getValue().doubleValue())).toList())
                .doOnNext(rs -> log.info("Получен ответ с информацией по курсам валют {}", rs))
                .doOnError(t -> log.error("Запрос на обновление курса валют завершился с ошибкой {}", t.getMessage()))
                .doOnError(ignore -> Counter.builder("currency_rate_error").register(meterRegistry).increment())
                .onErrorMap(t -> new IntegrationErrorException("exchange-service"))
                .block();
    }
}
