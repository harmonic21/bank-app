package ru.yandex.practicum.exchange.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.RecordInterceptor;
import ru.yandex.practicum.exchange.model.CurrencyInfo;

@Slf4j
public class KafkaEventInterceptor implements RecordInterceptor<String, CurrencyInfo> {

    @Override
    public ConsumerRecord<String, CurrencyInfo> intercept(ConsumerRecord<String, CurrencyInfo> record,
                                                          Consumer<String, CurrencyInfo> consumer) {
        log.info("Получено событие. Партиция {}, оффесет {}, топик {}, тело {}", record.partition(), record.offset(), record.topic(), record.value());
        return record;
    }

    @Override
    public void success(ConsumerRecord<String, CurrencyInfo> record,
                        Consumer<String, CurrencyInfo> consumer) {
        log.info("Событие обработано успешно");
        RecordInterceptor.super.success(record, consumer);
    }

    @Override
    public void failure(ConsumerRecord<String, CurrencyInfo> record,
                        Exception exception,
                        Consumer<String, CurrencyInfo> consumer) {
        log.error("Ошибка при обработке события", exception);
        RecordInterceptor.super.failure(record, exception, consumer);
    }
}
