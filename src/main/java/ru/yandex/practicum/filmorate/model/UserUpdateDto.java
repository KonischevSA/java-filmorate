package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateDto {
    @NotNull
    @Positive
    Long id;
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Неверный формат поля email.")
    String email;
    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "Поле login содержит недопустимые символы.")
    String login;
    String name;
    @PastOrPresent(message = "Поле birthday не может быть больше текущей даты.")
    LocalDate birthday;
}
