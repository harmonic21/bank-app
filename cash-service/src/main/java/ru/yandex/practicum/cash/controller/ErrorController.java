package ru.yandex.practicum.cash.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.yandex.practicum.cash.error.OperationFailedException;
import ru.yandex.practicum.cash.model.ErrorResponseInfo;

@ControllerAdvice
public class ErrorController {

    @ResponseBody
    @ExceptionHandler(exception = OperationFailedException.class)
    public ErrorResponseInfo handleUserNotFoundException(OperationFailedException e) {
        return new ErrorResponseInfo(e.getMessage());
    }
}
