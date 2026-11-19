package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateDto {
    @NotNull
    @Positive
    private Long id;
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Неверный формат поля email.")
    private String email;
    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "Поле login содержит недопустимые символы.")
    private String login;
    private String name;
    @PastOrPresent(message = "Поле birthday не может быть больше текущей даты.")
    private LocalDate birthday;
}
