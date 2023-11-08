package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    private final UserDto userForAllTests = new UserDto(1L, "test", "test");

    @Test
    void getAllUsers_whenSuccess() {

        User user = new User();

        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDto> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals(UserMapper.toUserDto(user), users.get(0));
        verify(userRepository, times(1)).findAll();

    }

    @Test
    void save_whenSuccess() {
        User user = UserMapper.toUser(userForAllTests);

        when(userRepository.save(user)).thenReturn(user);

        UserDto getUser = userService.save(userForAllTests);

        assertEquals(userForAllTests, getUser);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUser_whenSuccess() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(UserMapper.toUser(userForAllTests)));

        UserDto updateData = new UserDto();
        updateData.setName("updatedName");
        updateData.setEmail("updatedEmail");

        User toUpdate = UserMapper.toUser(userForAllTests);
        toUpdate.setName("updatedName");
        toUpdate.setEmail("updatedEmail");

        when(userRepository.save(toUpdate)).thenReturn(toUpdate);

        UserDto afterUpdate = userService.updateUser(updateData, 1L);

        assertEquals(afterUpdate.getName(), "updatedName");
        assertEquals(afterUpdate.getEmail(), "updatedEmail");
    }

    @Test
    void getUserById_whenSuccess() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(UserMapper.toUser(userForAllTests)));

        UserDto getUser = userService.getUserById(1L);

        assertEquals(userForAllTests, getUser);
    }

    @Test
    void getUserById_whenNotFound() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void deleteUserById_whenSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(UserMapper.toUser(userForAllTests)));

        userService.deleteUserById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}