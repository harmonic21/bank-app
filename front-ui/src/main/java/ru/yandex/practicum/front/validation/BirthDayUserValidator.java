package ru.yandex.practicum.front.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BirthDayUserValidator implements ConstraintValidator<AdultBirthDay, LocalDate> {

    @Override
    public void initialize(AdultBirthDay constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return ChronoUnit.YEARS.between(localDate, LocalDate.now()) >= 18;
    }
}
