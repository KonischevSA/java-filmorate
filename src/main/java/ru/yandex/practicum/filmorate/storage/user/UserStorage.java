package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    Collection<User> findAllUsers();

    Optional<User> findUserById(Long id);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByLogin(String login);

    void addUser(User user);

    void updateUser(Long userId, String newEmail, String newLogin, String newName, LocalDate newBirthDay);

    void removeUser(Long userId);

    long getNextId();
}
