package ru.yandex.practicum.front.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.yandex.practicum.front.validation.AdultBirthDay;
import ru.yandex.practicum.front.validation.PasswordConfirmed;

import java.time.LocalDate;

@Data
@PasswordConfirmed
public class SignupUserInfoDto {
    @NotNull
    private String login;
    @NotNull
    private String password;
    @NotNull
    private String confirmPassword;
    @NotNull
    private String name;
    @NotNull
    @AdultBirthDay
    private LocalDate birthDate;
}
