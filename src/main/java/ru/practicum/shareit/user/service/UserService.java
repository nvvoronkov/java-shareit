package ru.practicum.shareit.user.service;

import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto add(UserDto userDto);

    UserDto update(UserDto userDtoForUpdate);

    UserDto getById(Long userId) throws ObjectNotFoundException;

    List<UserDto> getAll();

    String delete(Long userId);

    void checkIsUserInStorage(Long userId);

}
