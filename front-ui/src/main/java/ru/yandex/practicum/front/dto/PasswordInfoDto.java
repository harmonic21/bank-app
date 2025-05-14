package ru.yandex.practicum.front.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.yandex.practicum.front.validation.PasswordConfirmed;

@Data
@PasswordConfirmed
public class PasswordInfoDto {
    @NotNull
    private String password;
    @NotNull
    private String confirmPassword;
}
