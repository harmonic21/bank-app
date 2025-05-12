package ru.yandex.practicum.front.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SignupUserInfoDto {
    private String login;
    private String password;
    private String confirmPassword;
    private String name;
    private LocalDate birthDate;
}
