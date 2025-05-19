package ru.yandex.practicum.front.error;

public class IntegrationErrorException extends RuntimeException {
    private static final String MSG = "При попытке вызова %s получена ошибка. Попробуйте повторить позже";

    public IntegrationErrorException(String system) {
        super(MSG.formatted(system));
    }
}
