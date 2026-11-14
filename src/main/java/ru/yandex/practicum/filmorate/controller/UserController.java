package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserUpdateDto;

import java.util.*;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findUsers() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {

        log.info("Получен запрос на создание пользователя:\n{}", user.toString());

        if (user.getId() != null) {
            log.info("Получено непустое значение поле id = {}. При добавлении нового пользователя оно будет проигнорировано.", user.getId());
        }

        Optional<User> tmpUser = users.values().stream()
                .filter(u -> Objects.equals(u.getEmail(), user.getEmail()))
                .findFirst();
        if (tmpUser.isPresent()) {
            log.warn("Значение поля email \"{}\" уже используется.", user.getEmail());
            throw new DuplicatedDataException("Значение поля email " + user.getEmail() + " уже используется.");
        }

        tmpUser = users.values().stream()
                .filter(u -> Objects.equals(u.getLogin(), user.getLogin()))
                .findFirst();
        if (tmpUser.isPresent()) {
            log.warn("Значение поля login \"{}\" уже используется.", user.getLogin());
            throw new DuplicatedDataException("Значение поля login " + user.getLogin() + " уже используется.");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.warn("Отсутствует значение поля name. Вместо него использовано значение поля login.");
        }

        user.setId(getNextId());
        users.put(user.getId(), user);

        log.info("Добавлен новый пользователь:\n{}", user);

        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody UserUpdateDto userUpdateDto) {

        log.info("Получен запрос на обновление информации о пользователе:\n{}", userUpdateDto);

        if (users.containsKey(userUpdateDto.getId())) {

            User oldUser = users.get(userUpdateDto.getId());
            log.info("Найден пользователь с id = {}\n{}", oldUser.getId(), oldUser);

            if (userUpdateDto.getEmail() != null && !userUpdateDto.getEmail().isBlank() && !oldUser.getEmail().equals(userUpdateDto.getEmail())) {
                log.info("Обновлено значение поля email. Новое значение: {}", userUpdateDto.getEmail());
                oldUser.setEmail(userUpdateDto.getEmail());
            }
            if (userUpdateDto.getLogin() != null && !userUpdateDto.getLogin().isBlank() && !oldUser.getLogin().equals(userUpdateDto.getLogin())) {

                Optional<User> tmpUser = users.values().stream()
                        .filter(u -> Objects.equals(u.getLogin(), userUpdateDto.getLogin()))
                        .findFirst();

                if (tmpUser.isPresent() && !tmpUser.get().getId().equals(userUpdateDto.getId())) {
                    log.warn("Значение поля login \"{}\" уже используется.", userUpdateDto.getLogin());
                    throw new DuplicatedDataException("Значение поля login " + userUpdateDto.getLogin() + " уже используется.");
                }

                if (userUpdateDto.getName() == null && oldUser.getName().equals(oldUser.getLogin())) {
                    log.info("Отсутствует информация по обновлению поля name. Текущее значение совпадает с полем login. Поле name будет обновлено из поля login.");
                    oldUser.setName(userUpdateDto.getLogin());
                }

                log.info("Обновлено значение поля login. Новое значение: {}", userUpdateDto.getLogin());
                oldUser.setLogin(userUpdateDto.getLogin());
            }
            if (userUpdateDto.getName() != null && !userUpdateDto.getName().isBlank() && !oldUser.getName().equals(userUpdateDto.getName())) {
                log.info("Обновлено значение поля name. Новое значение: {}", userUpdateDto.getName());
                oldUser.setName(userUpdateDto.getName());
            }
            if (userUpdateDto.getBirthday() != null && !oldUser.getBirthday().equals(userUpdateDto.getBirthday())) {
                log.info("Обновлено значение поля birthday. Новое значение: {}", userUpdateDto.getBirthday());
                oldUser.setBirthday(userUpdateDto.getBirthday());
            }

            return oldUser;
        }

        log.warn("Пользователь с id = {} не найден.", userUpdateDto.getId());
        throw new NotFoundException("Пользователь с id = " + userUpdateDto.getId() + " не найден.");
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}

