package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentReturnDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemReturnDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    List<ItemReturnDto> getAllItemsByUser(long userId);

    ItemDto saveItem(long userId, ItemDto item);

    ItemDto updateItem(long userId, ItemDto item, long itemId);

    ItemReturnDto getItemInfo(long id, long userId);

    List<ItemDto> searchItems(String text);

    Item getItemIfExists(long id);

    CommentReturnDto createComment(long userId, long itemId, CommentDto commentDto);

}
