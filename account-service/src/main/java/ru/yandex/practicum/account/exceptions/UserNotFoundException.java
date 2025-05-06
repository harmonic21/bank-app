package ru.yandex.practicum.account.exceptions;

public class UserNotFoundException extends RuntimeException {
    private static final String MSG_TEMPLATE = "Пользователь с именем %s не найден";

    public UserNotFoundException(String username) {
        super(MSG_TEMPLATE.formatted(username));
    }
}
