package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    @Positive
    private Long id;
    @NotBlank(message = "Отсутствует значение поля email.")
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Неверный формат поля email.")
    private String email;
    @NotBlank(message = "Поле login не может быть пустым.")
    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "Поле login содержит недопустимые символы.")
    private String login;
    private String name;
    @NotNull
    @PastOrPresent(message = "Поле birthday не может быть больше текущей даты.")
    private LocalDate birthday;

    private Set<Long> friends = new HashSet<>();
}
