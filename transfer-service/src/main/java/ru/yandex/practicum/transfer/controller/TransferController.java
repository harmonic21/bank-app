package ru.yandex.practicum.transfer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.transfer.api.TransferApi;
import ru.yandex.practicum.transfer.model.ResponseInfo;
import ru.yandex.practicum.transfer.model.TransferCashRq;
import ru.yandex.practicum.transfer.service.TransferService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TransferController implements TransferApi {

    private final TransferService transferService;

    @Override
    public ResponseEntity<ResponseInfo> transferCash(String fromUsername,
                                                     String toUsername,
                                                     TransferCashRq transferCashRq) {
        log.info("Получен запрос на перевод средств между {} и {}", fromUsername, toUsername);
        transferService.transfer(fromUsername, toUsername, transferCashRq);
        return ResponseEntity.ok(new ResponseInfo(true));
    }
}
