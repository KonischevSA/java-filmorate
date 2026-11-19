package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
@Getter
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> findAllUsers() {
        return users.values().stream().toList();
    }

    @Override
    public Optional<User> findUserById(Long id) {
        User user = users.get(id);

        return user == null ? Optional.empty() : Optional.of(user);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return users.values().stream()
                .filter(u -> Objects.equals(u.getEmail(), email))
                .findFirst();
    }

    @Override
    public Optional<User> findUserByLogin(String login) {
        return users.values().stream()
                .filter(u -> Objects.equals(u.getLogin(), login))
                .findFirst();
    }

    @Override
    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public void updateUser(Long userId, String newEmail, String newLogin, String newName, LocalDate newBirthDay) {
        User user = users.get(userId);

        if (newEmail != null) {
            user.setEmail(newEmail);
            log.info("Обновлено значение поля email. Новое значение: {}", newEmail);
        }
        if (newLogin != null) {
            user.setLogin(newLogin);
            log.info("Обновлено значение поля login. Новое значение: {}", newLogin);
        }
        if (newName != null) {
            user.setName(newName);
            log.info("Обновлено значение поля name. Новое значение: {}", newName);
        }
        if (newBirthDay != null) {
            user.setBirthday(newBirthDay);
            log.info("Обновлено значение поля birthday. Новое значение: {}", newBirthDay);
        }
    }

    @Override
    public void removeUser(Long userId) {
        users.remove(userId);
    }

    @Override
    public long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
