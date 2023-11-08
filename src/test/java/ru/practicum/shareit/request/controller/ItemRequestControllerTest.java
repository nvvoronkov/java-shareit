package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestRequesterDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestService itemRequestService;

    ItemRequestDto itemRequestForTests = new ItemRequestDto(1L, "test", LocalDateTime.now());

    ItemRequestRequesterDto itemRequestRequesterDto = ItemRequestMapper.toItemRequestRequesterDto(new ItemRequest(1L, "test", null, LocalDateTime.now()), List.of());

    @SneakyThrows
    @Test
    void saveRequest() {
        when(itemRequestService.saveRequest(anyLong(), any(ItemRequestDto.class))).thenReturn(itemRequestForTests);

        String result = mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "123")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemRequestForTests)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemRequestForTests), result);
    }

    @SneakyThrows
    @Test
    void getAllRequesterRequests() {
        List<ItemRequestRequesterDto> allRequests = List.of(itemRequestRequesterDto);

        when(itemRequestService.getAllRequesterRequests(anyLong())).thenReturn(allRequests);

        String result = mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", "123"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(allRequests), result);
    }

    @SneakyThrows
    @Test
    void getAllRequests() {
        List<ItemRequestRequesterDto> allRequests = List.of(itemRequestRequesterDto);

        when(itemRequestService.getAllRequests(anyLong(), anyInt(), anyInt())).thenReturn(allRequests);

        String result = mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "123"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(allRequests), result);
    }

    @SneakyThrows
    @Test
    void getRequesterItemRequest() {
        when(itemRequestService.getRequesterItemRequest(anyLong(), anyLong())).thenReturn(itemRequestRequesterDto);

        String result = mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", "123"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemRequestRequesterDto), result);
    }
}