package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentReturnDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemReturnDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    private final ItemReturnDto itemReturnDto = new ItemReturnDto(1L,
            "Test Item",
            "Test Description",
            true, null, null, List.of());

    private final ItemDto itemDto = new ItemDto(1L, "Test Item", "Test Description", true, null);


    @SneakyThrows
    @Test
    void getAllItemsByUser() {

        List<ItemReturnDto> items = List.of(itemReturnDto);

        when(itemService.getAllItemsByUser(anyLong(), anyInt(), anyInt())).thenReturn(items);


        String result = mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", "123"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(items), result);

    }

    @SneakyThrows
    @Test
    void saveItem() {

        when(itemService.saveItem(anyLong(), any(ItemDto.class))).thenReturn(itemDto);

        String result = mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "123")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);

    }

    @SneakyThrows
    @Test
    void updateItem() {

        when(itemService.updateItem(anyLong(), any(ItemDto.class), anyLong())).thenReturn(itemDto);

        String result = mockMvc.perform(patch("/items/{id}", 1L)
                        .header("X-Sharer-User-Id", "123")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    @SneakyThrows
    @Test
    void getItemInfo() {
        when(itemService.getItemInfo(anyLong(), anyLong())).thenReturn(itemReturnDto);

        String result = mockMvc.perform(get("/items/{id}", 1L)
                        .header("X-Sharer-User-Id", "123"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemReturnDto), result);
    }

    @SneakyThrows
    @Test
    void searchItems() {

        when(itemService.searchItems(anyString(), anyInt(), anyInt())).thenReturn(List.of(itemDto));

        String result = mockMvc.perform(get("/items/search")
                        .param("text", "test"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(itemDto)), result);
    }

    @SneakyThrows
    @Test
    void createComment() {

        CommentDto commentDto = new CommentDto();
        commentDto.setText("test");

        CommentReturnDto commentReturnDto = new CommentReturnDto();
        commentReturnDto.setText("test");


        when(itemService.createComment(123, 1L, commentDto)).thenReturn(commentReturnDto);

        String result = mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", "123")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(commentReturnDto), result);
    }
}