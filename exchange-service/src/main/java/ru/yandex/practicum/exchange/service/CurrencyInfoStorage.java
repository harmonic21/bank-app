package ru.yandex.practicum.exchange.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.exchange.model.CurrencyInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CurrencyInfoStorage {

    private static final Map<String, CurrencyInfo> CURRENCY_INFO = new HashMap<>();

    public void updateCurrencyInfo(List<CurrencyInfo> currencyInfoList) {
        currencyInfoList.forEach(currency -> CURRENCY_INFO.put(currency.getName(), currency));
    }

    public List<CurrencyInfo> getCurrencyInfo() {
        return new ArrayList<>(CURRENCY_INFO.values());
    }
}
