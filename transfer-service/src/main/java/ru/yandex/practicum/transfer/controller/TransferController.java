package ru.yandex.practicum.transfer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.transfer.api.TransferApi;
import ru.yandex.practicum.transfer.model.ResponseInfo;
import ru.yandex.practicum.transfer.model.TransferCashRq;
import ru.yandex.practicum.transfer.service.TransferService;

@RestController
@RequiredArgsConstructor
public class TransferController implements TransferApi {

    private final TransferService transferService;

    @Override
    public ResponseEntity<ResponseInfo> transferCash(String fromUsername,
                                                     String toUsername,
                                                     TransferCashRq transferCashRq) {
        transferService.transfer(fromUsername, toUsername, transferCashRq);
        return ResponseEntity.ok(new ResponseInfo(true));
    }
}
