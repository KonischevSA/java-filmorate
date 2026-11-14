package ru.yandex.practicum.filmorate.customAnnotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.yandex.practicum.filmorate.validators.MatchDateValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MatchDateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MatchDate {
    String value();
    String message() default "Дата должна быть не раньше {value}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
