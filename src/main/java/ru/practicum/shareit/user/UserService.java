package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ConflictErrorException;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.interfaces.Mapper;
import ru.practicum.shareit.interfaces.Services;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements Services<UserDto> {
    private final UserStorage userStorage;
    private final Mapper<UserDto, User> userMapper;

    @Override
    public UserDto add(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        if (userStorage.checkIsUserEmailInStorage(user.getEmail())) {
            log.warn(String.format("Пользователь с email=%s уже существует.", user.getEmail()));
            throw new ConflictErrorException(String.format("Пользователь с email=%s уже существует.", user.getEmail()));
        }
        User createdUser = userStorage.add(user);
        log.info(String.format("Пользователь id=%s успешно добавлен.", userDto.getId()));
        return userMapper.toDto(createdUser);
    }

    @Override
    public UserDto update(UserDto userDtoForUpdate) {
        if (!userStorage.checkIsObjectInStorage(userDtoForUpdate.getId())) {
            log.warn(String.format("Пользователь id=%s не найден.", userDtoForUpdate.getId()));
            throw new ObjectNotFoundException(String.format("Пользователь id=%s не найден.", userDtoForUpdate.getId()));
        }
        User userFromStorage = userStorage.getById(userDtoForUpdate.getId());
        if (userDtoForUpdate.getEmail() == null) {
            userDtoForUpdate.setEmail(userFromStorage.getEmail());
        } else if (userStorage.checkIsUserEmailInStorage(userDtoForUpdate.getEmail(), userDtoForUpdate.getId())) {
            log.warn(String.format("Пользователь с email=%s уже существует.", userDtoForUpdate.getEmail()));
            throw new ConflictErrorException(String.format("Пользователь с email=%s уже существует.",
                    userDtoForUpdate.getEmail()));
        }
        userDtoForUpdate.setName(Optional.ofNullable(userDtoForUpdate.getName()).orElse(userFromStorage.getName()));
        User userForUpdate = userMapper.toEntity(userDtoForUpdate);
        User updatedUser = userStorage.update(userForUpdate);
        log.info(String.format("Пользователь id=%s успешно обновлен.", userDtoForUpdate.getId()));
        return userMapper.toDto(updatedUser);
    }

    @Override
    public UserDto getById(Long userId) throws ObjectNotFoundException {
        if (userStorage.checkIsObjectInStorage(userId)) {
            User user = userStorage.getById(userId);
            log.info(String.format("Получены данные пользователя id=%s.", userId));
            return userMapper.toDto(user);
        } else {
            log.warn(String.format("Пользователь id=%s не найден.", userId));
            throw new ObjectNotFoundException(String.format("Пользователь id=%s не найден.", userId));
        }
    }

    @Override
    public List<UserDto> getAll() {
        List<User> listOfUsers = userStorage.getAll();
        log.info("Получены список всех пользователей");
        return listOfUsers.stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public String delete(Long userId) {
        if (userStorage.checkIsObjectInStorage(userId)) {
            String message = userStorage.delete(userId);
            log.info(message);
            return message;
        } else {
            log.warn(String.format("Пользователь id=%s не найден.", userId));
            throw new ObjectNotFoundException(String.format("Пользователь id=%s не найден.", userId));
        }
    }
}
