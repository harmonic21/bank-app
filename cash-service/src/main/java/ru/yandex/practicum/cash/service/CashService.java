package ru.yandex.practicum.cash.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.account.api.AccountApi;
import ru.yandex.practicum.blocker.api.CheckApi;
import ru.yandex.practicum.blocker.model.CheckOperationRq;
import ru.yandex.practicum.blocker.model.CheckResultRs;
import ru.yandex.practicum.cash.error.OperationFailedException;
import ru.yandex.practicum.cash.model.ChangeCashRq;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CashService {

    private final CheckApi checkApi;
    private final AccountApi accountApi;

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
        } else {
            throw new OperationFailedException(checkResult.getReason());
        }

    }
}
