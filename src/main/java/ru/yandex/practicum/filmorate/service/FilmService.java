package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmUpdateDto;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    public FilmService(FilmStorage filmStorage, UserService userService) {

        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Collection<Film> findAllFilms() {
        return filmStorage.findALlFilms();
    }

    public Film findFilmById(Long id) {
        Optional<Film> film = filmStorage.findFilmById(id);

        if (film.isEmpty()) {
            log.warn("Фильм с id = {} не найден.", id);
            throw new NotFoundException("Фильм с id = " + id + " не найден.");
        }

        return film.get();
    }

    public Film create(Film film) {
        log.info("Получен запрос на добавление фильма:\n{}", film);

        if (filmStorage.checkDuplicate(film)) {
            log.warn("Данный фильм уже добавлен ранее:\n{}", film);
            throw new DuplicatedDataException("Данный фильм уже добавлен ранее.");
        }

        film.setId(filmStorage.getNextId());
        filmStorage.addFilm(film);

        log.info("Добавлен новый фильм:\n{}", film);

        return film;
    }

    public Film update(FilmUpdateDto filmUpdateDto) {
        log.info("Получен запрос на обновление информации о фильме:\n{}", filmUpdateDto);

        Optional<Film> film = filmStorage.findFilmById(filmUpdateDto.getId());

        if (film.isPresent()) {

            Film oldFilm = film.get();
            log.info("Найден фильм с id = {}\n{}", oldFilm.getId(), oldFilm);

            Optional<Film> tmpFilm = filmStorage.findFilmByFields(filmUpdateDto.getName() == null ? oldFilm.getName() : filmUpdateDto.getName(),
                    filmUpdateDto.getDescription() == null ? oldFilm.getDescription() : filmUpdateDto.getDescription(),
                    filmUpdateDto.getReleaseDate() == null ? oldFilm.getReleaseDate() : filmUpdateDto.getReleaseDate(),
                    filmUpdateDto.getDuration() == null ? oldFilm.getDuration() : filmUpdateDto.getDuration());

            if (tmpFilm.isPresent()
                    && ((filmUpdateDto.getName() != null && !filmUpdateDto.getName().isBlank())
                    || (filmUpdateDto.getDescription() != null && !filmUpdateDto.getDescription().isBlank())
                    || filmUpdateDto.getReleaseDate() != null
                    || filmUpdateDto.getDuration() != null)) {
                log.warn("Данный фильм уже добавлен ранее:\n{}", oldFilm);
                throw new DuplicatedDataException("Данный фильм уже добавлен ранее.");
            }

            String newName = null;
            String newDescription = null;
            LocalDate newReleaseDate = null;
            Integer newDuration = null;

            if (filmUpdateDto.getName() != null && !filmUpdateDto.getName().isBlank() && !oldFilm.getName().equals(filmUpdateDto.getName())) {
                newName = filmUpdateDto.getName();
            }
            if (filmUpdateDto.getDescription() != null && !filmUpdateDto.getDescription().isBlank() &&
                    !oldFilm.getDescription().equals(filmUpdateDto.getDescription())) {
                newDescription = filmUpdateDto.getDescription();
            }
            if (filmUpdateDto.getReleaseDate() != null && !oldFilm.getReleaseDate().equals(filmUpdateDto.getReleaseDate())) {
                newReleaseDate = filmUpdateDto.getReleaseDate();
            }
            if (filmUpdateDto.getDuration() != null && !oldFilm.getDuration().equals(filmUpdateDto.getDuration())) {
                newDuration = filmUpdateDto.getDuration();
            }

            filmStorage.updateFilm(oldFilm.getId(), newName, newDescription, newReleaseDate, newDuration);

            log.info("Обновленный фильм: \n{}", oldFilm);

            return oldFilm;
        }

        log.warn("Фильм с id = {} не найден.", filmUpdateDto.getId());
        throw new NotFoundException("Фильм с id = " + filmUpdateDto.getId() + " не найден.");
    }

    public Film addLike(Long id, Long userId) {
        Optional<Film> film = filmStorage.findFilmById(id);

        if (film.isPresent()) {
            //вызов findUserById чисто для проверки наличия пользователя, дабы не плодить чекалки в сервисах
            userService.findUserById(userId);
            film.get().getLikes().add(userId);
        } else {
            log.warn("Фильм с id = {} не найден.", id);
            throw new NotFoundException("Фильм с id = " + id + " не найден.");
        }

        return film.get();
    }

    public Film removeLike(Long id, Long userId) {
        Optional<Film> film = filmStorage.findFilmById(id);

        if (film.isPresent()) {
            userService.findUserById(userId);
            film.get().getLikes().remove(userId);
        } else {
            log.warn("Фильм с id = {} не найден.", id);
            throw new NotFoundException("Фильм с id = " + id + " не найден.");
        }

        return film.get();
    }

    public Collection<Film> getPopularFilms(int count) {
        return filmStorage.findPopularFilms(count);
    }

    public void removeFilm(Long id) {
        Optional<Film> film = filmStorage.findFilmById(id);

        if (film.isPresent()) {
            log.info("Удаление фильма: \n{}", film.get());
            filmStorage.removeFilm(id);
        } else {
            log.warn("Фильм с id = {} не найден.", id);
            throw new NotFoundException("Фильм с id = " + id + " не найден.");
        }
    }
}
