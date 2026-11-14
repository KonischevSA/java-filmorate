package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmUpdateDto;

import java.time.*;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findFilms() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {

        log.info("Получен запрос на добавление фильма:\n{}", film.toString());

        Optional<Film> tmpFilm = films.values().stream()
                .filter(u -> u.getName().equals(film.getName()) &&
                        u.getDescription().equals(film.getDescription()) &&
                        u.getReleaseDate().equals(film.getReleaseDate()) &&
                        u.getDuration().equals(film.getDuration()))
                .findFirst();
        if (tmpFilm.isPresent()) {
            log.warn("Данный фильм уже добавлен ранее:\n{}", film);
            throw new DuplicatedDataException("Данный фильм уже добавлен ранее.");
        }

        film.setId(getNextId());
        films.put(film.getId(), film);

        log.info("Добавлен новый фильм:\n{}", film);

        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody FilmUpdateDto filmUpdateDto) {

        log.info("Получен запрос на обновление информации о фильме:\n{}", filmUpdateDto);

        if (films.containsKey(filmUpdateDto.getId())) {

            Film oldFilm = films.get(filmUpdateDto.getId());
            log.info("Найден фильм с id = {}\n{}", oldFilm.getId(), oldFilm);

            Film tmpFilm = new Film();
            tmpFilm.setName(filmUpdateDto.getName() == null ? oldFilm.getName() : filmUpdateDto.getName());
            tmpFilm.setDescription(filmUpdateDto.getDescription() == null ? oldFilm.getDescription() : filmUpdateDto.getDescription());
            tmpFilm.setReleaseDate(filmUpdateDto.getReleaseDate() == null ? oldFilm.getReleaseDate() : filmUpdateDto.getReleaseDate());
            tmpFilm.setDuration(filmUpdateDto.getDuration() == null ? oldFilm.getDuration() : filmUpdateDto.getDuration());

            Optional<Film> optFilm = films.values().stream()
                    .filter(u -> u.getName().equals(tmpFilm.getName()) &&
                            u.getDescription().equals(tmpFilm.getDescription()) &&
                            u.getReleaseDate().equals(tmpFilm.getReleaseDate()) &&
                            u.getDuration().equals(tmpFilm.getDuration()) &&
                            !u.getId().equals(oldFilm.getId()))
                    .findFirst();
            if (optFilm.isPresent()) {
                log.warn("Данный фильм уже добавлен ранее:\n{}", oldFilm);
                throw new DuplicatedDataException("Данный фильм уже добавлен ранее.");
            }

            if (filmUpdateDto.getName() != null && !filmUpdateDto.getName().isBlank() && !oldFilm.getName().equals(filmUpdateDto.getName())) {
                log.info("Обновлено значение поля name. Новое значение: {}", filmUpdateDto.getName());
                oldFilm.setName(filmUpdateDto.getName());
            }
            if (filmUpdateDto.getDescription() != null && !filmUpdateDto.getDescription().isBlank() &&
                    !oldFilm.getDescription().equals(filmUpdateDto.getDescription())) {
                log.info("Обновлено значение поля description. Новое значение: {}", filmUpdateDto.getDescription());
                oldFilm.setDescription(filmUpdateDto.getDescription());
            }
            if (filmUpdateDto.getReleaseDate() != null && !oldFilm.getReleaseDate().equals(filmUpdateDto.getReleaseDate())) {
                log.info("Обновлено значение поля releaseDate. Новое значение: {}", filmUpdateDto.getReleaseDate());
                oldFilm.setReleaseDate(filmUpdateDto.getReleaseDate());
            }
            if (filmUpdateDto.getDuration() != null && !oldFilm.getDuration().equals(filmUpdateDto.getDuration())) {
                log.info("Обновлено значение поля duration. Новое значение: {}", filmUpdateDto.getDuration());
                oldFilm.setDuration(filmUpdateDto.getDuration());
            }

            return oldFilm;
        }

        log.warn("Фильм с id = {} не найден.", filmUpdateDto.getId());
        throw new NotFoundException("Фильм с id = " + filmUpdateDto.getId() + " не найден.");
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
