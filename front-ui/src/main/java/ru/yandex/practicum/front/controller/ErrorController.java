package ru.yandex.practicum.front.controller;

import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(exception = MethodArgumentNotValidException.class)
    public ModelAndView signupFormNotValid(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .toList();
        ModelAndView modelAndView = new ModelAndView("signup.html");
        modelAndView.addObject("errors", errors);
        modelAndView.addObject("userInfo", ex.getBindingResult().getTarget());
        return modelAndView;
    }
}
