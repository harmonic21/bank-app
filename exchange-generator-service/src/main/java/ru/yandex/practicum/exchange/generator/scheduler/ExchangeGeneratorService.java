package ru.yandex.practicum.exchange.generator.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exchange.api.CurrencyApi;
import ru.yandex.practicum.exchange.model.CurrencyInfo;
import ru.yandex.practicum.exchange.model.UpdateCurrencyRq;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ExchangeGeneratorService {

    private static final CurrencyInfo RUB_INFO = new CurrencyInfo().name("Рубль").shortName("RUB").value(BigDecimal.ONE);
    private static final Random RANDOM = new Random();

    private final CurrencyApi currencyApi;

    @Scheduled(fixedDelay = 1L, timeUnit = TimeUnit.SECONDS)
    public void generateActualExchangeInfo() {
        UpdateCurrencyRq request = new UpdateCurrencyRq()
                .addCurrencyInfoItem(RUB_INFO)
                .addCurrencyInfoItem(new CurrencyInfo().name("Юань").shortName("CNY").value(BigDecimal.valueOf(RANDOM.nextDouble(100))))
                .addCurrencyInfoItem(new CurrencyInfo().name("Доллар").shortName("USD").value(BigDecimal.valueOf(RANDOM.nextDouble(100))));
        currencyApi.updateCurrencyInfo(request).block();
    }
}
