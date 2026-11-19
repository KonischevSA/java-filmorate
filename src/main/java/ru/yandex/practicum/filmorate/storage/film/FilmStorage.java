package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    Collection<Film> findALlFilms();

    Optional<Film> findFilmById(Long id);

    Optional<Film> findFilmByFields(String name, String description, LocalDate releaseDate, Integer duration);

    boolean checkDuplicate(Film film);

    void addFilm(Film film);

    void updateFilm(Long filmId, String newName, String newDescription, LocalDate newReleaseDate, Integer newDuration);

    void removeFilm(Long filmId);

    long getNextId();

    Collection<Film> findPopularFilms(int count);
}
