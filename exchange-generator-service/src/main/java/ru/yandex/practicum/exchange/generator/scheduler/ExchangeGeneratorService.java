package ru.yandex.practicum.exchange.generator.scheduler;

import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exchange.model.CurrencyInfo;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeGeneratorService {

    private static final CurrencyInfo RUB_INFO = new CurrencyInfo().name("Рубль").shortName("RUB").value(BigDecimal.ONE);
    private static final Random RANDOM = new Random();

    private final KafkaTemplate<String, CurrencyInfo> kafkaTemplate;
    private final Tracer trace;

    @Value("${kafka.producer.topic-name}")
    private String topicName;

    @Scheduled(fixedDelay = 5L, timeUnit = TimeUnit.SECONDS)
    public void generateActualExchangeInfo() {
        log.info("Отправляем событие в {} о курсе рубля", topicName);
        sendEventAndTrace(() -> kafkaTemplate.send(topicName, RUB_INFO), "kafka.%s.RUB".formatted(topicName));
        log.info("Отправляем событие в {} о курсе юаня", topicName);
        var cnyEvent = new CurrencyInfo().name("Юань").shortName("CNY").value(BigDecimal.valueOf(RANDOM.nextDouble(100)));
        sendEventAndTrace(() -> kafkaTemplate.send(topicName, cnyEvent), "kafka.%s.CNY".formatted(topicName));
        log.info("Отправляем событие в {} о курсе доллара", topicName);
        var usdEvent = new CurrencyInfo().name("Доллар").shortName("USD").value(BigDecimal.valueOf(RANDOM.nextDouble(100)));
        sendEventAndTrace(() -> kafkaTemplate.send(topicName, usdEvent), "kafka.%s.USD".formatted(topicName));
    }

    private void sendEventAndTrace(Runnable runnable, String name) {
        var span = trace.nextSpan().remoteServiceName("kafka").name(name).start();
        try {
            runnable.run();
        } finally {
            span.end();
        }
    }
}
