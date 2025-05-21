package ru.yandex.practicum.front.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.yandex.practicum.account.model.AccountDetailInfo;
import ru.yandex.practicum.account.model.UserInfoRs;
import ru.yandex.practicum.front.constants.AccountCurrency;
import ru.yandex.practicum.front.dto.AccountInfoDto;
import ru.yandex.practicum.front.dto.PasswordInfoDto;
import ru.yandex.practicum.front.dto.PersonalUserInfoDto;
import ru.yandex.practicum.front.service.UserAccountService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserAccountService userAccountService;

    @GetMapping("/")
    public String getIndexPage() {
        return "redirect:/main";
    }

    @GetMapping("/main")
    public String getMainPage(Model model,
                              Authentication authentication) {
        var userDetailInfo = userAccountService.getUserInfoDetailByUserName(authentication.getName());
        var personalInfo = userDetailInfo.map(userDetail -> new PersonalUserInfoDto()
                        .setName(userDetail.getFullName())
                        .setBirthDate(userDetail.getBirthDay()))
                .orElse(new PersonalUserInfoDto());
        var accountsInfo = userDetailInfo.map(UserInfoRs::getAccounts)
                .map(this::mapAccountInfo)
                .orElse(Collections.emptyList());
        model.addAttribute("personalInfo", personalInfo.setAccounts(accountsInfo));
//        model.addAttribute("accounts", accountsInfo);
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
        } else {
            userAccountService.updatePersonalUserInfo(login, personalUserInfoDto);
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

    private List<AccountInfoDto> mapAccountInfo(List<AccountDetailInfo> accounts) {
        return Arrays.stream(AccountCurrency.values())
                .map(currency -> accounts.stream()
                        .filter(accountInfo -> Objects.equals(accountInfo.getCurrency(), currency.name()))
                        .findFirst()
                        .map(accountInfo -> new AccountInfoDto().setCurrency(currency).setValue(ObjectUtils.firstNonNull(accountInfo.getBalance(), BigDecimal.ZERO)).setExists(true))
                        .orElse(AccountInfoDto.newAccount(currency))
                ).toList();
    }
}
