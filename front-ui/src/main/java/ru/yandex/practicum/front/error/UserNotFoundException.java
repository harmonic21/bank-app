package ru.yandex.practicum.front.error;

public class UserNotFoundException extends RuntimeException{

    private static final String MSG = """
            Пользователь с логином %s не найден.
            Вы можете зарегистрировать нового пользователя по адресу http://localhost:8080/signup""";

    public UserNotFoundException(String userName) {
        super(MSG.formatted(userName));
    }
}
