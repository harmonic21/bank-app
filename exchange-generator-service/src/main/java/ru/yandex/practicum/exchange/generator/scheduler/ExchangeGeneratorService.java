package ru.yandex.practicum.exchange.generator.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ExchangeGeneratorService {

    @Scheduled(fixedDelay = 1L, timeUnit = TimeUnit.SECONDS)
    public void generateActualExchangeInfo() {

    }
}
