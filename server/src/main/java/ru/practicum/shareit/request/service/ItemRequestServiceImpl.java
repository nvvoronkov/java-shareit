package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestRequesterDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public ItemRequestDto saveRequest(long requesterId, ItemRequestDto itemRequestDto) {
        log.info("request received: POST /requests");
        User requester = userService.getUserByIdIfExists(requesterId);
        ItemRequest forReturn = itemRequestRepository.save(ItemRequestMapper.toItemRequest(itemRequestDto, requester));
        return ItemRequestMapper.toItemRequestDto(forReturn);
    }

    @Override
    public List<ItemRequestRequesterDto> getAllRequesterRequests(long requesterId) {

        log.info("request received: GET /requests");
        User requester = userService.getUserByIdIfExists(requesterId);
        List<ItemRequest> allRequests = itemRequestRepository.findAllByRequester(requester);

        Map<Long, List<Item>> itemRequestsMap = mappingItemsForRequests(allRequests);

        return allRequests.stream()
                .map(itemRequest -> ItemRequestMapper.toItemRequestRequesterDto(itemRequest, mappingList(itemRequestsMap.get(itemRequest.getId()))))
                .sorted(Comparator.comparing(ItemRequestRequesterDto::getCreated).reversed())
                .collect(toList());
    }

    @Override
    public ItemRequestRequesterDto getRequesterItemRequest(long requesterId, long requestId) {

        log.info("request received: GET /requests/{requestId}");

        userService.getUserByIdIfExists(requesterId);
        ItemRequest forReturn = getItemRequestIfExists(requestId);

        List<Item> items = itemRepository.findAllByRequest(forReturn);

        return ItemRequestMapper.toItemRequestRequesterDto(forReturn, mappingList(items));
    }

    @Override
    public List<ItemRequestRequesterDto> getAllRequests(long requesterId, int from, int size) {

        log.info("request received: GET /requests?from={from}&size={size}");

        User requester = userService.getUserByIdIfExists(requesterId);

        List<ItemRequest> allRequests = itemRequestRepository.findAllByRequesterNot(requester, PageRequest.of(from / size, size)).stream()
                .sorted(Comparator.comparing(ItemRequest::getCreated).reversed())
                .collect(toList());

        Map<Long, List<Item>> itemRequestsMap = mappingItemsForRequests(allRequests);

        return allRequests.stream()
                .map(itemRequest -> ItemRequestMapper.toItemRequestRequesterDto(itemRequest, mappingList(itemRequestsMap.get(itemRequest.getId()))))
                .sorted(Comparator.comparing(ItemRequestRequesterDto::getCreated).reversed())
                .collect(toList());
    }

    private List<ItemDto> mappingList(List<Item> items) {
        if (items == null || items.isEmpty())
            return List.of();

        return items.stream().map(ItemMapper::toItemDto).collect(toList());
    }

    public ItemRequest getItemRequestIfExists(long requestId) {
        return itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new DataNotFoundException("Запрос с id " + requestId + " не найден"));
    }

    private Map<Long, List<Item>> mappingItemsForRequests(List<ItemRequest> allRequests) {
        return itemRepository.findAllByRequestInOrderByIdAsc(allRequests)
                .stream()
                .collect(groupingBy(it -> it.getRequest().getId(), toList()));

    }
}
