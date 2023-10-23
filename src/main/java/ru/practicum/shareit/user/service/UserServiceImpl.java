package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RequiredArgsConstructor
@Slf4j
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    public List<UserDto> getAllUsers() {
        log.info("выполняется запрос в базу данных на поулчение спика всех пользователей");
        List<User> forRet = userRepository.findAll();
        return forRet.stream()
                .flatMap(u -> {
                    UserDto dto = UserMapper.toUserDto(u);
                    return Stream.of(dto);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserDto save(UserDto userDto) {

        User userForSave = UserMapper.toUser(userDto);

        log.info("выполняется запрос на добавление нового пользователя");

        User userForReturn = userRepository.save(userForSave);
        return UserMapper.toUserDto(userForReturn);
    }

    @Transactional
    @Override
    public UserDto updateUser(UserDto userDto, long id) {

        log.info("выполняется запрос на обновление пользователя | UserID - {}", id);

        User userForUpdate = getUserByIdIfExists(id);

        if (userDto.getName() != null) {
            userForUpdate.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            userForUpdate.setEmail(userDto.getEmail());
        }

        User userForReturn = userRepository.save(userForUpdate);

        log.info("Пользватель с ID {} обновлен {}", userDto.getId(), userDto);

        return UserMapper.toUserDto(userForReturn);
    }

    public UserDto getUserById(long id) {

        log.info("выполняется запрос на поулучение ползователья | UserId - {}", id);

        User userForReturn = getUserByIdIfExists(id);

        return UserMapper.toUserDto(userForReturn);
    }

    @Transactional
    @Override
    public void deleteUserById(long id) {
        log.info("выполняется запрос на удаление ползователья | UserId - {}", id);
        getUserByIdIfExists(id);
        userRepository.deleteById(id);
    }

    @Override
    public User getUserByIdIfExists(long id) {
        return userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("пользователь с id " + id + " не найден"));

    }

}
