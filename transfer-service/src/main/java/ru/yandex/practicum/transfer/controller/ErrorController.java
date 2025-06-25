package ru.yandex.practicum.transfer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.transfer.error.OperationFailedException;
import ru.yandex.practicum.transfer.model.ResponseInfo;
import ru.yandex.practicum.transfer.model.ResponseInfoError;

@Slf4j
@ControllerAdvice
public class ErrorController {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(exception = OperationFailedException.class)
    public ResponseInfo handleUserNotFoundException(OperationFailedException e) {
        log.error("При работе сервиса произошла ошибка: {}", e.getMessage());
        return new ResponseInfo().status(false).error(new ResponseInfoError().errorMessage(e.getMessage()));
    }
}
