package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.customAnnotations.MatchDate;

import java.time.LocalDate;

@Data
public class FilmUpdateDto {
    @NotNull
    @Positive
    Long id;
    String name;
    @Size(max = Film.MAX_FILM_DESCRIPTION_LENGTH, message = "Поле description не должно быть больше " + Film.MAX_FILM_DESCRIPTION_LENGTH + " символов.")
    String description;
    @MatchDate("1895-12-28")
    LocalDate releaseDate;
    @Positive(message = "Поле duration должно быть положительным целым числом.")
    Integer duration;
}
