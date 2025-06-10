package ru.yandex.practicum.front.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferService {

    public void doTransfer(String from, String to, String fromCurrency, String toCurrency, BigDecimal transferValue) {

    }
}
