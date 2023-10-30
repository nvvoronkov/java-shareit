package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerITest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @SneakyThrows
    @Test
    void save_whenSuccess() {

        UserDto forSave = new UserDto(1L, "test", "test");

        when(userService.save(forSave)).thenReturn(forSave);

        String result = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(forSave)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(forSave), result);
    }

    @SneakyThrows
    @Test
    void getAllUsers() {
        List<UserDto> forRet = List.of(new UserDto(1L, "test", "test"));

        when(userService.getAllUsers()).thenReturn(forRet);

        String result = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(forRet), result);
    }

    @SneakyThrows
    @Test
    void updateUser() {
        UserDto forUpdate = new UserDto(1L, "test", "test");

        when(userService.updateUser(forUpdate, 1L)).thenReturn(forUpdate);

        String result = mockMvc.perform(patch("/users/{id}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(forUpdate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(forUpdate), result);
    }


    @SneakyThrows
    @Test
    void getUserById_whenSuccess() {
        long id = 1L;
        mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().isOk());

        verify(userService).getUserById(id);
    }
}