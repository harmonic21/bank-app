package ru.yandex.practicum.cash.error;

public class OperationFailedException extends RuntimeException {

    private static final String MSG = "Операция не выполнена. %s";

    public OperationFailedException(String message) {
        super(MSG.formatted(message));
    }
}
