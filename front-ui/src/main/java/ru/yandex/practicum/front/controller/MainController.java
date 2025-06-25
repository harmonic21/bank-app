package ru.yandex.practicum.front.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.account.model.AccountDetailInfo;
import ru.yandex.practicum.account.model.UserInfo;
import ru.yandex.practicum.account.model.UserInfoRs;
import ru.yandex.practicum.cash.model.ResponseInfo;
import ru.yandex.practicum.front.constants.AccountCurrency;
import ru.yandex.practicum.front.dto.AccountInfoDto;
import ru.yandex.practicum.front.dto.PasswordInfoDto;
import ru.yandex.practicum.front.dto.PersonalUserInfoDto;
import ru.yandex.practicum.front.service.TransferService;
import ru.yandex.practicum.front.service.UserAccountService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserAccountService userAccountService;
    private final TransferService transferService;

    @GetMapping("/")
    public String getIndexPage() {
        return "redirect:/main";
    }

    @RequestMapping(value = "/main", method = {RequestMethod.GET, RequestMethod.POST})
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
            log.error("При попытке редактировать пароль получены ошибки: {}", errors);
            model.addAttribute("passwordErrors", errors);
            return "main.html";
        }
        userAccountService.updateUserPassword(login, passwordInfoDto.getPassword());
        return "redirect:/main";
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
            log.error("При попытке изменить информацию о пользователе получены ошибки: {}", errors);
            model.addAttribute("userAccountsErrors", errors);
        } else {
            userAccountService.updatePersonalUserInfo(login, personalUserInfoDto);
        }
        return "main.html";
    }

    @PostMapping("/user/{login}/сash")
    public String changeCashOnAccount(@PathVariable(name = "login") String login,
                                      @ModelAttribute(name = "action") String action,
                                      @ModelAttribute(name = "value") BigDecimal value,
                                      @ModelAttribute(name = "cashCurrency") String cashCurrency,
                                      BindingResult bindingResult,
                                      Model model) {
        var result = userAccountService.updateCash(action, login, cashCurrency, value);
        if (BooleanUtils.isFalse(result.getStatus())) {
            log.error("При проведении операций с наличными деньгами получены ошибки: {}", result.getError().getErrorMessage());
            model.addAttribute("cashErrors", List.of(result.getError().getErrorMessage()));
            return "main.html";
        }
        return "redirect:/";
    }

    @PostMapping("/user/{login}/transfer")
    public String transferMoney(@PathVariable(name = "login") String login,
                                @ModelAttribute(name = "from_currency") String fromCurrency,
                                @ModelAttribute(name = "to_currency") String toCurrency,
                                @ModelAttribute(name = "value") BigDecimal transferValue,
                                @ModelAttribute(name = "to_login") String toLogin,
                                BindingResult bindingResult,
                                Model model) {
        var result = transferService.doTransfer(login, toLogin, fromCurrency, toCurrency, transferValue);
        if (BooleanUtils.isFalse(result.getStatus())) {
            log.error("При попытке перевода средств получены ошибки: {}", result.getError().getErrorMessage());
            if (Objects.equals(login, toLogin)) {
                model.addAttribute("transferErrors", List.of(result.getError().getErrorMessage()));
            } else {
                model.addAttribute("transferOtherErrors", List.of(result.getError().getErrorMessage()));
            }
            return "main.html";
        }
        return "redirect:/";
    }

    @ModelAttribute("passwordInfo")
    public PasswordInfoDto createPasswordInfoDto() {
        return new PasswordInfoDto();
    }

    @ModelAttribute("login")
    public String getLoginFromAuthentication(Authentication authentication) {
        return authentication.getName();
    }

    @ModelAttribute
    public void prepareMainModel(Authentication authentication,
                                 Model model) {
        var userDetailInfo = userAccountService.getUserInfoDetailByUserName(authentication.getName());
        var personalInfo = userDetailInfo.map(UserInfoRs::getUserInfo)
                .map(userDetail -> new PersonalUserInfoDto()
                        .setName(userDetail.getFullName())
                        .setBirthDate(userDetail.getBirthDay())
                        .setEmail(userDetail.getEmail()))
                .orElse(new PersonalUserInfoDto());
        var accountsInfo = userDetailInfo.map(UserInfoRs::getUserInfo)
                .map(UserInfo::getAccounts)
                .map(this::mapAccountInfo)
                .orElse(Collections.emptyList());
        var currency = accountsInfo.stream()
                .filter(AccountInfoDto::isExists)
                .map(AccountInfoDto::getCurrency)
                .toList();
        model.addAttribute("personalInfo", personalInfo.setAccounts(accountsInfo));
        model.addAttribute("currency", currency);
        model.addAttribute("users", userAccountService.getAllRegisteredUsers(authentication.getName()));
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
