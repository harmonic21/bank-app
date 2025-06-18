package ru.yandex.practicum.notification.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.notification.model.SendNotificationRq;
import ru.yandex.practicum.notification.service.EmailService;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExchangeListener {

    private final EmailService emailService;

    @KafkaListener(topics = "${kafka.consumer.topic-name}")
    public void handleEvent(@Payload SendNotificationRq payload) {
        emailService.sendNotification(payload.getUserMail(), payload.getSubject(), payload.getText());
    }
}
