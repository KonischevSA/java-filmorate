package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.customAnnotations.MatchDate;

import java.time.LocalDate;

@Data
public class Film {
    public static final int MAX_FILM_DESCRIPTION_LENGTH = 200;
    @Positive
    Long id;
    @NotBlank(message = "Поле name не может быть пустым.")
    String name;
    @Size(max = MAX_FILM_DESCRIPTION_LENGTH, message = "Поле description не должно быть больше " + MAX_FILM_DESCRIPTION_LENGTH + " символов.")
    String description;
    @MatchDate("1895-12-28")
    LocalDate releaseDate;
    @NotNull
    @Positive(message = "Поле duration должно быть положительным целым числом.")
    Integer duration;
}
