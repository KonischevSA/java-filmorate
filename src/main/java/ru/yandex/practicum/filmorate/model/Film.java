package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.customAnnotations.MatchDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
public class Film {
    public static final int MAX_FILM_DESCRIPTION_LENGTH = 200;
    @Positive
    private Long id;
    @NotBlank(message = "Поле name не может быть пустым.")
    private String name;
    @Size(max = MAX_FILM_DESCRIPTION_LENGTH, message = "Поле description не должно быть больше " + MAX_FILM_DESCRIPTION_LENGTH + " символов.")
    private String description;
    @MatchDate("1895-12-28")
    private LocalDate releaseDate;
    @NotNull
    @Positive(message = "Поле duration должно быть положительным целым числом.")
    private Integer duration;

    private Set<Long> likes = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return Objects.equals(name, film.name) && Objects.equals(description, film.description) && Objects.equals(releaseDate, film.releaseDate) && Objects.equals(duration, film.duration);
    }
}
