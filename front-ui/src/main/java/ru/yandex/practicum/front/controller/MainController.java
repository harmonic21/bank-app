package ru.yandex.practicum.front.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Validated
@RequestMapping
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/")
    public String getIndexPage() {
        return "redirect:/main";
    }

    @GetMapping("/main")
    public String getMainPage() {
        return "main.html";
    }
}
