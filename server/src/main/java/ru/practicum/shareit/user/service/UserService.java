package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
public interface UserService {
    UserDto save(UserDto user);

    List<UserDto> getAllUsers();

    UserDto updateUser(UserDto user, long id);

    UserDto getUserById(long id);

    void deleteUserById(long id);

    User getUserByIdIfExists(long id);

}
