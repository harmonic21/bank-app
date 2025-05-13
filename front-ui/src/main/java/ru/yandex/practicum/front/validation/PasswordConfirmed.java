package ru.yandex.practicum.front.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordConfirmedValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordConfirmed {
    String message() default "Пароли не совпадают!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
