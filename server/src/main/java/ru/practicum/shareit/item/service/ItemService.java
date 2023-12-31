package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentReturnDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemReturnDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    List<ItemReturnDto> getAllItemsByUser(long ownerId, int from, int size);

    ItemDto saveItem(long userId, ItemDto item);

    ItemDto updateItem(long userId, ItemDto item, long itemId);

    ItemReturnDto getItemInfo(long id, long userId);

    List<ItemDto> searchItems(String text, int from, int size);

    Item getItemIfExists(long id);

    CommentReturnDto createComment(long userId, long itemId, CommentDto commentDto);
}
