package ru.yandex.practicum.front.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ShortUserInfoDto {
    private String login;
    private String name;
}
