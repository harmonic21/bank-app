package ru.yandex.practicum.front.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.yandex.practicum.front.dto.SignupUserInfoDto;
import ru.yandex.practicum.front.service.SignupUserService;

@Validated
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
    public String signupNewUser(@Valid @ModelAttribute SignupUserInfoDto userInfo) {
        signupUserService.signupNewUser(userInfo);
        return "redirect:/";
    }
}
