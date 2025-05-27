package ru.yandex.practicum.cash.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.account.api.AccountApi;
import ru.yandex.practicum.account.api.UserApi;
import ru.yandex.practicum.account.model.UserInfo;
import ru.yandex.practicum.account.model.UserInfoRs;
import ru.yandex.practicum.blocker.api.CheckApi;
import ru.yandex.practicum.blocker.model.CheckOperationRq;
import ru.yandex.practicum.blocker.model.CheckResultRs;
import ru.yandex.practicum.cash.error.OperationFailedException;
import ru.yandex.practicum.cash.model.ChangeCashRq;
import ru.yandex.practicum.notification.api.NotificationApi;
import ru.yandex.practicum.notification.model.SendNotificationRq;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CashService {

    private final CheckApi checkApi;
    private final AccountApi accountApi;
    private final NotificationApi notificationApi;
    private final UserApi userApi;

    public void updateCash(ChangeCashRq request) {
        CheckResultRs checkResult = checkApi.checkClientOperation(new CheckOperationRq()
                .requestAction(request.getAction())
                .requestService("cash-service")
                .requestUser(request.getUserName())
        ).block();

        if (checkResult.getApproved()) {
            var accountRequest = new ru.yandex.practicum.account.model.ChangeCashRq()
                    .currency(request.getCurrency())
                    .value(request.getValue());
            if (Objects.equals("GET", request.getAction())) {
                accountApi.getCashFromAccount(request.getUserName(), accountRequest).block();
            } else if (Objects.equals("PUT", request.getAction())) {
                accountApi.addCashToAccount(request.getUserName(), accountRequest).block();
            }
            sendUserNotification(request, getUserInfo(request.getUserName()));
        } else {
            throw new OperationFailedException(checkResult.getReason());
        }

    }

    private UserInfo getUserInfo(String userName) {
        return userApi.getUserInfoByUsername(userName).map(UserInfoRs::getUserInfo).block();
    }

    private void sendUserNotification(ChangeCashRq requestInfo, UserInfo userInfo) {
        String subject = "";
        String text = "";
        if (Objects.equals("GET", requestInfo.getAction())) {
            subject = "С вашего %s счета сняты наличные.";
            text = "Уважаемый %s, уведомляем Вас, что с Вашего счета %s сняты наличные в размере %s";
        } else if (Objects.equals("PUT", requestInfo.getAction())) {
            subject = "На ваш %s счет внесены наличные.";
            text = "Уважаемый %s, уведомляем Вас, что на Ваш счет %s внесены наличные в размере %s";
        }
        notificationApi.sendNotification(
                new SendNotificationRq()
                        .userMail(userInfo.getEmail())
                        .subject(subject.formatted(requestInfo.getCurrency()))
                        .text(text.formatted(userInfo.getFullName(), requestInfo.getCurrency(), requestInfo.getValue()))
        ).block();
    }
}
