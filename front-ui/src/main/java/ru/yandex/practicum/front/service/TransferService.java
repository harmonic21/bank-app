package ru.yandex.practicum.front.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.front.util.RestClientUtility;
import ru.yandex.practicum.transfer.api.TransferApi;
import ru.yandex.practicum.transfer.model.ResponseInfo;
import ru.yandex.practicum.transfer.model.TransferCashRq;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferApi transferApi;

    public ResponseInfo doTransfer(String from, String to, String fromCurrency, String toCurrency, BigDecimal transferValue) {
        var request = new TransferCashRq()
                .fromAccount(fromCurrency)
                .toAccount(toCurrency)
                .value(transferValue);
        return transferApi.transferCash(from, to, request)
                .onErrorResume(
                        RestClientUtility::isWebClientResponseException,
                        t -> Mono.just(RestClientUtility.getResponseBodyFromError(t, ResponseInfo.class))
                )
                .block();
    }
}
