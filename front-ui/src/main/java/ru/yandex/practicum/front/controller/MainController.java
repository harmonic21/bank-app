package ru.yandex.practicum.front.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

@Validated
@Controller
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/")
    public String getIndexPage() {
        return "redirect:/main";
    }

    @GetMapping("/main")
    public String getMainPage(Authentication authentication,
                              Model model) {
        model.addAttribute("login", authentication.getName());
        return "main.html";
    }
}
