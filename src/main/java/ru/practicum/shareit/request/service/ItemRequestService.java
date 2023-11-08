package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestRequesterDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto saveRequest(long requesterId, ItemRequestDto itemRequestDto);

    List<ItemRequestRequesterDto> getAllRequesterRequests(long requesterId);

    ItemRequestRequesterDto getRequesterItemRequest(long requesterId, long requestId);

    List<ItemRequestRequesterDto> getAllRequests(long requesterId, int from, int size);

    ItemRequest getItemRequestIfExists(long requestId);
}
