package ru.yandex.practicum.exchange.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exchange.model.CurrencyInfo;
import ru.yandex.practicum.exchange.service.CurrencyInfoStorage;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExchangeListener {

    private final CurrencyInfoStorage currencyInfoStorage;

    @KafkaListener(topics = "${kafka.consumer.topic-name}")
    public void handleEvent(@Payload CurrencyInfo payload) {
        currencyInfoStorage.updateCurrencyInfo(payload);
    }
}
