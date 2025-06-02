package ru.yandex.practicum.front.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CurrencyRateDto {
    private String title;
    private String name;
    private Double value;
}
