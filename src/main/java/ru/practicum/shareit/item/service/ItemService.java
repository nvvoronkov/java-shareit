package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemResponseResponseDto add(ItemRequestDto itemRequestDto);

    ItemResponseResponseDto update(ItemRequestDto itemRequestDto);

    ResponseDto getById(Long userId, Long itemId);

    List<ItemResponseResponseDtoForOwner> getAllByOwner(Long userId);

    CommentResponseDto addComment(CommentRequestDto commentRequestDto);

    List<ItemResponseResponseDto> search(String text);

    String delete(Long userId);

    void checkIsItemInStorage(Long itemId);

    void checkIsItemAvailable(Long itemId);

    public void checkIsItemValid(Long itemId);

    Item findById(Long itemId);
}
