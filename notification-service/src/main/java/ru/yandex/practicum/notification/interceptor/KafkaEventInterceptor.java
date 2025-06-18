package ru.yandex.practicum.notification.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.RecordInterceptor;
import ru.yandex.practicum.notification.model.SendNotificationRq;

@Slf4j
public class KafkaEventInterceptor implements RecordInterceptor<String, SendNotificationRq> {

    @Override
    public ConsumerRecord<String, SendNotificationRq> intercept(ConsumerRecord<String, SendNotificationRq> record,
                                                          Consumer<String, SendNotificationRq> consumer) {
        log.info("Получено событие. Партиция {}, оффесет {}, топик {}, тело {}", record.partition(), record.offset(), record.topic(), record.value());
        return record;
    }

    @Override
    public void success(ConsumerRecord<String, SendNotificationRq> record,
                        Consumer<String, SendNotificationRq> consumer) {
        log.info("Событие обработано успешно");
        RecordInterceptor.super.success(record, consumer);
    }

    @Override
    public void failure(ConsumerRecord<String, SendNotificationRq> record,
                        Exception exception,
                        Consumer<String, SendNotificationRq> consumer) {
        log.error("Ошибка при обработке события", exception);
        RecordInterceptor.super.failure(record, exception, consumer);
    }
}
