package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserUpdateDto;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public User findUserById(@Valid @PathVariable Long id) {
        return userService.findUserById(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> findUserFriends(@Valid @PathVariable Long id) {
        return userService.findUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> findUsersCommonFriends(@Valid @PathVariable Long id, @Valid @PathVariable Long otherId) {
        return userService.findUsersCommonFriends(id, otherId);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody UserUpdateDto userUpdateDto) {
        return userService.update(userUpdateDto);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@Valid @PathVariable Long id, @Valid @PathVariable Long friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFriend(@Valid @PathVariable Long id, @Valid @PathVariable Long friendId) {
        return userService.removeFriend(id, friendId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUser(@Valid @PathVariable Long id) {
        userService.removeUser(id);
    }
}

