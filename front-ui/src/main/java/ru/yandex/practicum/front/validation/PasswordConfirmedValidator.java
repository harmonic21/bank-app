package ru.yandex.practicum.front.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.front.dto.SignupUserInfoDto;

import java.util.Objects;

public class PasswordConfirmedValidator implements ConstraintValidator<PasswordConfirmed, SignupUserInfoDto> {

    @Override
    public void initialize(PasswordConfirmed constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(SignupUserInfoDto userInfo, ConstraintValidatorContext constraintValidatorContext) {
        return Objects.equals(userInfo.getPassword(), userInfo.getConfirmPassword());
    }
}
