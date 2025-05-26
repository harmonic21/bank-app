package ru.yandex.practicum.account.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.account.exceptions.UserNotFoundException;
import ru.yandex.practicum.account.model.ErrorResponseInfo;
import ru.yandex.practicum.account.model.ResponseInfo;
import ru.yandex.practicum.account.model.ResponseInfoError;

@ControllerAdvice
public class ErrorController {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(exception = UserNotFoundException.class)
    public ErrorResponseInfo handleUserNotFoundException(UserNotFoundException e) {
        return new ErrorResponseInfo(
                new ResponseInfo(false).error(new ResponseInfoError(e.getMessage()))
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(exception = DataIntegrityViolationException.class)
    public ErrorResponseInfo handleWrongDataBaseParams(DataIntegrityViolationException e) {
        return new ErrorResponseInfo(
                new ResponseInfo(false).error(new ResponseInfoError("Пользователь с таким логином уже существует"))
        );
    }
}
