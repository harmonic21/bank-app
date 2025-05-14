package ru.yandex.practicum.front.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.yandex.practicum.front.validation.AdultBirthDay;

import java.time.LocalDate;

@Data
public class SignupUserInfoDto {
    @NotNull
    private String login;
    @Valid
    private PasswordInfoDto passwordInfo = new PasswordInfoDto();
    @NotNull
    private String name;
    @NotNull
    @AdultBirthDay
    private LocalDate birthDate;
}
