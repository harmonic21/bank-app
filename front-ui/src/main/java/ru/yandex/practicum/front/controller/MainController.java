package ru.yandex.practicum.front.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.yandex.practicum.front.dto.PasswordInfoDto;

@Controller
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

    @PostMapping("/user/{login}/editPassword")
    public String editPassword(@Valid @ModelAttribute(name = "passwordInfo") PasswordInfoDto passwordInfoDto,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList();
            model.addAttribute("passwordErrors", errors);
        }
        return "main.html";
    }

    @ModelAttribute("passwordInfo")
    public PasswordInfoDto createPasswordInfoDto() {
        return new PasswordInfoDto();
    }

    @ModelAttribute("login")
    public String getLoginFromAuthentication(Authentication authentication) {
        return authentication.getName();
    }
}
