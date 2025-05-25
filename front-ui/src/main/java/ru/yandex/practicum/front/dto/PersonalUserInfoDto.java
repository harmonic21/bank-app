package ru.yandex.practicum.front.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
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
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate birthDate;
    @Email
    @NotNull
    private String email;
    private List<AccountInfoDto> accounts;
}
