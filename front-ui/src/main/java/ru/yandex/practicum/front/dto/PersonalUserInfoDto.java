package ru.yandex.practicum.front.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.yandex.practicum.front.validation.AdultBirthDay;

import java.time.LocalDate;
import java.util.List;

@Data
@Accessors(chain = true)
public class PersonalUserInfoDto {
    @NotNull
    private String name;
    @NotNull
    @AdultBirthDay
    private LocalDate birthDate;
    private List<AccountInfoDto> accounts;
}
