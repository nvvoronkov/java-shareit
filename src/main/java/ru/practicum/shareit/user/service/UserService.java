package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto save(UserDto user);

    List<UserDto> getAllUsers();

    UserDto updateUser(UserDto user, long id);

    UserDto getUserById(long id);

    void deleteUserById(long id);

    User getUserByIdIfExists(long id);

}
