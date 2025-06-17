package ru.yandex.practicum.exchange.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.exchange.model.CurrencyInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class CurrencyInfoStorage {

    private static final Map<String, CurrencyInfo> CURRENCY_INFO = new HashMap<>();

    public void updateCurrencyInfo(List<CurrencyInfo> currencyInfoList) {
        currencyInfoList.forEach(this::updateCurrencyInfo);
    }

    public void updateCurrencyInfo(CurrencyInfo currencyInfo) {
        CURRENCY_INFO.put(currencyInfo.getName(), currencyInfo);
    }

    public List<CurrencyInfo> getCurrencyInfo() {
        return new ArrayList<>(CURRENCY_INFO.values());
    }

    public BigDecimal exchangeCurrency(String from, String to, BigDecimal value) {
        var rub = exchangeToRub(from, value);
        return exchangeTo(to, rub);
    }

    private BigDecimal exchangeToRub(String fromCurrency, BigDecimal value) {
        return value.multiply(findCurrencyByShortName(fromCurrency).getValue());
    }

    private BigDecimal exchangeTo(String currency, BigDecimal value) {
        return value.divide(findCurrencyByShortName(currency).getValue(), RoundingMode.HALF_UP);
    }

    private CurrencyInfo findCurrencyByShortName(String shortName) {
        return CURRENCY_INFO.values().stream()
                .filter(currency -> Objects.equals(currency.getShortName(), shortName))
                .findFirst()
                .orElse(null);
    }
}
