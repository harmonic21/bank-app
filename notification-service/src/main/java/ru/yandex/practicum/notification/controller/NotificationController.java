package ru.yandex.practicum.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.notification.api.NotificationApi;
import ru.yandex.practicum.notification.model.SendNotificationRq;
import ru.yandex.practicum.notification.service.EmailService;

@RestController
@RequiredArgsConstructor
public class NotificationController implements NotificationApi {

    private final EmailService emailService;

    @Override
    public ResponseEntity<Void> sendNotification(SendNotificationRq sendNotificationRq) {
        emailService.sendNotification(sendNotificationRq.getUserMail(), sendNotificationRq.getSubject(), sendNotificationRq.getText());
        return ResponseEntity.ok().build();
    }
}
