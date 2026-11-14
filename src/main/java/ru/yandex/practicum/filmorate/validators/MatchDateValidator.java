package ru.yandex.practicum.filmorate.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.customAnnotations.MatchDate;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class MatchDateValidator implements ConstraintValidator<MatchDate, LocalDate> {

    private LocalDate targetDate;

    @Override
    public void initialize(MatchDate constraintAnnotation) {
        try {
            this.targetDate = LocalDate.parse(constraintAnnotation.value());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Неверный формат даты в аннотации. Используйте YYYY-MM-DD", e);
        }
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return value.isEqual(targetDate) || value.isAfter(targetDate);
    }
}
