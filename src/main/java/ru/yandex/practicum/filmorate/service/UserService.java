package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserUpdateDto;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    public User findUserById(Long id) {
        Optional<User> user = userStorage.findUserById(id);

        if (user.isEmpty()) {
            log.warn("Пользователь с id = {} не найден.", id);
            throw new NotFoundException("Пользователь с id = " + id + " не найден.");
        }

        return user.get();
    }

    public User create(User user) {

        log.info("Получен запрос на создание пользователя:\n{}", user.toString());

        if (user.getId() != null) {
            log.info("Получено непустое значение поле id = {}. При добавлении нового пользователя оно будет проигнорировано.", user.getId());
        }

        Optional<User> tmpUser = userStorage.findUserByEmail(user.getEmail());
        if (tmpUser.isPresent()) {
            log.warn("Значение поля email \"{}\" уже используется.", user.getEmail());
            throw new DuplicatedDataException("Значение поля email " + user.getEmail() + " уже используется.");
        }

        tmpUser = userStorage.findUserByLogin(user.getLogin());
        if (tmpUser.isPresent()) {
            log.warn("Значение поля login \"{}\" уже используется.", user.getLogin());
            throw new DuplicatedDataException("Значение поля login " + user.getLogin() + " уже используется.");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.warn("Отсутствует значение поля name. Вместо него использовано значение поля login.");
        }

        user.setId(userStorage.getNextId());
        userStorage.addUser(user);

        log.info("Добавлен новый пользователь:\n{}", user);

        return user;
    }

    public User update(UserUpdateDto userUpdateDto) {

        log.info("Получен запрос на обновление информации о пользователе:\n{}", userUpdateDto);

        //Получили пользователя с id из запроса на обновление
        Optional<User> user = userStorage.findUserById(userUpdateDto.getId());
        log.info("user \n{}", user);
        if (user.isPresent()) {

            User oldUser = user.get();
            log.info("Найден пользователь с id = {}\n{}", oldUser.getId(), oldUser);

            Optional<User> tmpUser;
            String newEmail = null;
            String newLogin = null;
            String newName = null;
            LocalDate newBirthDay = null;

            if (userUpdateDto.getEmail() != null && !userUpdateDto.getEmail().isBlank() && !oldUser.getEmail().equals(userUpdateDto.getEmail())) {

                tmpUser = userStorage.findUserByEmail(userUpdateDto.getEmail());
                //Пропустил отрицание во втором условии
                if (tmpUser.isPresent() && !tmpUser.get().getId().equals(userUpdateDto.getId())) {
                    log.warn("Значение поля email \"{}\" уже используется.", userUpdateDto.getEmail());
                    throw new DuplicatedDataException("Значение поля email " + userUpdateDto.getEmail() + " уже используется.");
                } else {
                    newEmail = userUpdateDto.getEmail();
                }
            }
            if (userUpdateDto.getLogin() != null && !userUpdateDto.getLogin().isBlank() && !oldUser.getLogin().equals(userUpdateDto.getLogin())) {
                //Поменял user на tmpUser, что бы было универсально. Работают оба варианта.
                tmpUser = userStorage.findUserByLogin(userUpdateDto.getLogin());

                if (tmpUser.isPresent() && !tmpUser.get().getId().equals(userUpdateDto.getId())) {
                    log.warn("Значение поля login \"{}\" уже используется.", userUpdateDto.getLogin());
                    throw new DuplicatedDataException("Значение поля login " + userUpdateDto.getLogin() + " уже используется.");
                }

                if (userUpdateDto.getName() == null && oldUser.getName().equals(oldUser.getLogin())) {
                    log.info("Отсутствует информация по обновлению поля name. Текущее значение совпадает с полем login. Поле name будет обновлено из поля login.");
                    newName = userUpdateDto.getLogin();
                }

                newLogin = userUpdateDto.getLogin();
            }
            if (userUpdateDto.getName() != null && !userUpdateDto.getName().isBlank() && !oldUser.getName().equals(userUpdateDto.getName())) {
                newName = userUpdateDto.getName();
            }
            if (userUpdateDto.getBirthday() != null && !oldUser.getBirthday().equals(userUpdateDto.getBirthday())) {
                newBirthDay = userUpdateDto.getBirthday();
            }

            userStorage.updateUser(oldUser.getId(), newEmail, newLogin, newName, newBirthDay);

            log.info("Обновленный пользователь \n{}", oldUser);

            return oldUser;
        }

        log.warn("Пользователь с id = {} не найден.", userUpdateDto.getId());
        throw new NotFoundException("Пользователь с id = " + userUpdateDto.getId() + " не найден.");
    }

    public User addFriend(Long id, Long friendId) {

        if (Objects.equals(id, friendId)) {
            log.warn("id пользователя не должно совпадать с id добавляемого друга");
            throw new DuplicatedDataException("id пользователя не должно совпадать с id добавляемого друга");
        }

        Optional<User> user = userStorage.findUserById(id);
        Optional<User> friend = userStorage.findUserById(friendId);

        if (user.isPresent()) {
            if (friend.isPresent()) {
                user.get().getFriends().add(friendId);
                friend.get().getFriends().add(id);
            } else {
                log.warn("Пользователь с id = {} не найден.", friendId);
                throw new NotFoundException("Пользователь с id = " + friendId + " не найден.");
            }
        } else {
            log.warn("Пользователь с id = {} не найден.", id);
            throw new NotFoundException("Пользователь с id = " + id + " не найден.");
        }

        return user.get();
    }

    public User removeFriend(Long id, Long friendId) {

        if (Objects.equals(id, friendId)) {
            log.warn("id пользователя не должно совпадать с id удаляемого друга");
            throw new DuplicatedDataException("id пользователя не должно совпадать с id удаляемого друга");
        }

        Optional<User> user = userStorage.findUserById(id);
        Optional<User> friend = userStorage.findUserById(friendId);

        if (user.isPresent()) {
            if (friend.isPresent()) {
                user.get().getFriends().remove(friendId);
                friend.get().getFriends().remove(id);
            } else {
                log.warn("Пользователь с id = {} не найден.", friendId);
                throw new NotFoundException("Пользователь с id = " + friendId + " не найден.");
            }
        } else {
            log.warn("Пользователь с id = {} не найден.", id);
            throw new NotFoundException("Пользователь с id = " + id + " не найден.");
        }

        return user.get();
    }

    public Collection<User> findUserFriends(Long id) {
        Optional<User> user = userStorage.findUserById(id);

        if (user.isPresent()) {
            return user.get().getFriends().stream()
                    .map(friendId -> userStorage.findUserById(friendId).get())
                    .collect(Collectors.toCollection(ArrayList::new));
        } else {
            log.warn("Пользователь с id = {} не найден.", id);
            throw new NotFoundException("Пользователь с id = " + id + " не найден.");
        }
    }

    public Collection<User> findUsersCommonFriends(Long id, Long otherId) {
        if (Objects.equals(id, otherId)) {
            log.warn("id пользователя не должно совпадать с id другого пользователя");
            throw new DuplicatedDataException("id пользователя не должно совпадать с id другого пользователя");
        }

        Optional<User> user = userStorage.findUserById(id);
        Optional<User> otherUser = userStorage.findUserById(otherId);

        if (user.isPresent()) {
            if (otherUser.isPresent()) {
                return user.get().getFriends().stream()
                        .filter(otherUser.get().getFriends()::contains)
                        .map(userId -> userStorage.findUserById(userId).get())
                        .collect(Collectors.toCollection(ArrayList::new));
            } else {
                log.warn("Пользователь с id = {} не найден.", otherId);
                throw new NotFoundException("Пользователь с id = " + otherId + " не найден.");
            }
        } else {
            log.warn("Пользователь с id = {} не найден.", id);
            throw new NotFoundException("Пользователь с id = " + id + " не найден.");
        }
    }

    public void removeUser(Long id) {
        Optional<User> user = userStorage.findUserById(id);

        if (user.isPresent()) {
            log.info("Удаление пользователя: \n{}", user.get());

            userStorage.findAllUsers().forEach(u -> u.getFriends().remove(id));
            userStorage.removeUser(id);
        } else {
            log.warn("Пользователь с id = {} не найден.", id);
            throw new NotFoundException("Пользователь с id = " + id + " не найден.");
        }
    }
}
