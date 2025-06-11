package ru.yandex.practicum.account.exceptions;

public class OperationBlockException extends RuntimeException {

    public OperationBlockException(String message) {
        super(message);
    }
}
