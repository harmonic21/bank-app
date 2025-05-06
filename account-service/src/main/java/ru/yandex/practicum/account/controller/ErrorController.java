package ru.yandex.practicum.account.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.account.exceptions.UserNotFoundException;
import ru.yandex.practicum.account.model.ErrorResponseInfo;

@ControllerAdvice
public class ErrorController {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(exception = UserNotFoundException.class)
    public ErrorResponseInfo handleUserNotFoundException(UserNotFoundException e) {
        return new ErrorResponseInfo(e.getMessage());
    }

}
