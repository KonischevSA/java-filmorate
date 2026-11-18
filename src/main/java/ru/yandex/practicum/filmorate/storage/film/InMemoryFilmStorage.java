package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.*;

import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private final Comparator<Film> filmPopularComparator = Comparator.comparingInt(film -> film.getLikes().size());

    @Override
    public Collection<Film> findALlFilms() {
        return films.values().stream().toList();
    }

    @Override
    public Optional<Film> findFilmById(Long id) {
        Film film = films.get(id);

        return film == null ? Optional.empty() : Optional.of(film);
    }

    @Override
    public Optional<Film> findFilmByFields(String name, String description, LocalDate releaseDate, Integer duration) {
        return films.values().stream()
                .filter(film -> film.getName().equals(name)
                        && film.getDescription().equals(description)
                        && film.getReleaseDate().equals(releaseDate)
                        && film.getDuration().equals(duration))
                .findFirst();
    }

    @Override
    public Collection<Film> findPopularFilms(int count) {
        return films.values().stream()
                .sorted(filmPopularComparator.reversed())
                .limit(count)
                .toList();
    }

    @Override
    public void addFilm(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public void updateFilm(Long filmId, String newName, String newDescription, LocalDate newReleaseDate, Integer newDuration) {
        Film film = films.get(filmId);

        if (newName != null) {
            film.setName(newName);
            log.info("Обновлено значение поля name. Новое значение: {}", newName);
        }
        if (newDescription != null) {
            film.setDescription(newDescription);
            log.info("Обновлено значение поля description. Новое значение: {}", newDescription);
        }
        if (newReleaseDate != null) {
            film.setReleaseDate(newReleaseDate);
            log.info("Обновлено значение поля releaseDate. Новое значение: {}", newReleaseDate);
        }
        if (newDuration != null) {
            film.setDuration(newDuration);
            log.info("Обновлено значение поля duration. Новое значение: {}", newDuration);
        }
    }

    public void removeFilm(Long filmId) {
        films.remove(filmId);
    }

    public boolean checkDuplicate(Film film) {
        Optional<Film> tmpFilm = films.values().stream()
                .filter(u -> u.equals(film))
                .findFirst();

        return tmpFilm.isPresent();
    }

    @Override
    public long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}

