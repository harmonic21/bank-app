package ru.yandex.practicum.front.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.yandex.practicum.front.dto.ErrorStorage;
import ru.yandex.practicum.front.dto.SignupUserInfoDto;
import ru.yandex.practicum.front.service.SignupUserService;

@Controller
@RequiredArgsConstructor
public class SignupController {

    private final SignupUserService signupUserService;

    @GetMapping("/signup")
    public String getSignupPage(Model model) {
        model.addAttribute("userInfo", new SignupUserInfoDto());
        return "signup.html";
    }

    @PostMapping("/signup")
    public String signupNewUser(@Valid @ModelAttribute(name = "userInfo") SignupUserInfoDto userInfo,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList();
            model.addAttribute("errors", errors);
            return "signup.html";
        }

        ErrorStorage errorStorage = signupUserService.signupNewUser(userInfo);
        if (errorStorage.hasError()) {
            model.addAttribute("userInfo", userInfo);
            model.addAttribute("errors", errorStorage.getErrors());
            return "signup.html";
        }
        return "redirect:/";
    }
}
