package ru.yandex.practicum.front.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BirthDayUserValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AdultBirthDay {
    String message() default "Доступ к сервису доступен лишь по достижению 18-ти лет";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
