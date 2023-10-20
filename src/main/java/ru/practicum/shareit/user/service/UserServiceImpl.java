package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto add(UserDto userDto) {
        User user = userMapper.dtoForOtherUsersToEntity(userDto);
        User createdUser = userRepository.save(user);
        log.info(String.format("Пользователь id=%s успешно добавлен.", createdUser.getId()));
        return userMapper.toDtoForOtherUsers(createdUser);
    }

    @Override
    public UserDto update(UserDto userDtoForUpdate) {
        checkIsUserInStorage(userDtoForUpdate.getId());
        User userFromStorage;
        Optional<User> optionalUserFromStorage = userRepository.findById(userDtoForUpdate.getId());
        if (optionalUserFromStorage.isPresent()) {
            userFromStorage = optionalUserFromStorage.get();
        } else {
            log.warn(String.format("Данные пользователя id=%s не найдены.", userDtoForUpdate.getId()));
            throw new ObjectNotFoundException(String.format("Данные пользователя id=%s не найдены.",
                    userDtoForUpdate.getId()));
        }
        if (userDtoForUpdate.getEmail() == null) {
            userDtoForUpdate.setEmail(userFromStorage.getEmail());
        }
        userDtoForUpdate.setName(Optional.ofNullable(userDtoForUpdate.getName()).orElse(userFromStorage.getName()));
        User userForUpdate = userMapper.dtoForOtherUsersToEntity(userDtoForUpdate);
        User updatedUser = userRepository.save(userForUpdate);
        log.info(String.format("Пользователь id=%s успешно обновлен.", userDtoForUpdate.getId()));
        return userMapper.toDtoForOtherUsers(updatedUser);
    }

    @Override
    public UserDto getById(Long userId) throws ObjectNotFoundException {
        if (userRepository.existsById(userId)) {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                log.info(String.format("Получены данные пользователя id=%s.", userId));
                return userMapper.toDtoForOtherUsers(user);
            } else {
                log.warn(String.format("Данные пользователя id=%s не найдены.", userId));
                throw new ObjectNotFoundException(String.format("Данные пользователя id=%s не найдены.", userId));
            }
        } else {
            log.warn(String.format("Пользователь id=%s не найден.", userId));
            throw new ObjectNotFoundException(String.format("Пользователь id=%s не найден.", userId));
        }
    }

    @Override
    public List<UserDto> getAll() {
        List<User> listOfUsers = userRepository.findAll();
        log.info("Получены список всех пользователей.");
        return listOfUsers.stream().map(userMapper::toDtoForOtherUsers).collect(Collectors.toList());
    }

    @Override
    public String delete(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            log.info(String.format("Пользователь id=%s успешно удален.", userId));
            return String.format("Пользователь id=%s успешно удален.", userId);
        } else {
            log.warn(String.format("Пользователь id=%s не найден.", userId));
            throw new ObjectNotFoundException(String.format("Пользователь id=%s не найден.", userId));
        }
    }

    @Override
    public void checkIsUserInStorage(Long userId) {
        if (!userRepository.existsById(userId)) {
            log.warn(String.format("Пользователь id=%s не найден.", userId));
            throw new ObjectNotFoundException(String.format("Пользователь id=%s не найден. ", userId));
        }
    }

    public User findById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            log.info(String.format("Получены данные пользователя id=%s.", userId));
            return user;
        } else {
            log.warn(String.format("Данные пользователя id=%s не найдены.", userId));
            throw new ObjectNotFoundException(String.format("Данные пользователя id=%s не найдены.", userId));
        }
    }
}
