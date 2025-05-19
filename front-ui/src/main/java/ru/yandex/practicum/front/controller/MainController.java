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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.yandex.practicum.front.dto.PasswordInfoDto;
import ru.yandex.practicum.front.dto.PersonalUserInfoDto;
import ru.yandex.practicum.front.service.UserAccountService;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserAccountService userAccountService;

    @GetMapping("/")
    public String getIndexPage() {
        return "redirect:/main";
    }

    @GetMapping("/main")
    public String getMainPage() {
        return "main.html";
    }

    @PostMapping("/user/{login}/editPassword")
    public String editPassword(@PathVariable(name = "login") String login,
                               @Valid @ModelAttribute(name = "passwordInfo") PasswordInfoDto passwordInfoDto,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList();
            model.addAttribute("passwordErrors", errors);
        }
        userAccountService.updateUserPassword(login, passwordInfoDto.getPassword());
        return "main.html";
    }

    @PostMapping("/user/{login}/editUserAccounts")
    public String editUserInfo(@PathVariable(name = "login") String login,
                               @Valid @ModelAttribute(name = "personalInfo") PersonalUserInfoDto personalUserInfoDto,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList();
            model.addAttribute("userAccountsErrors", errors);
        }
        userAccountService.updatePersonalUserInfo(login, personalUserInfoDto);
        return "main.html";
    }

    @ModelAttribute("passwordInfo")
    public PasswordInfoDto createPasswordInfoDto() {
        return new PasswordInfoDto();
    }

    @ModelAttribute("personalInfo")
    public PersonalUserInfoDto createPersonalUserInfoDto(Authentication authentication) {
        return userAccountService.getUserInfoDetailByUserName(authentication.getName())
                .map(userDetail -> new PersonalUserInfoDto()
                        .setName(userDetail.getFullName())
                        .setBirthDate(userDetail.getBirthDay()))
                .orElse(new PersonalUserInfoDto());
    }

    @ModelAttribute("login")
    public String getLoginFromAuthentication(Authentication authentication) {
        return authentication.getName();
    }
}
