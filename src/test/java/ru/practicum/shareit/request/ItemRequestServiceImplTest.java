package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestRequesterDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserService userService;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    private final User userForTests = new User(1L, "test", "test");

    private final ItemRequest itemRequestForTests = new ItemRequest(1L, "test", userForTests, LocalDateTime.now());


    @Test
    void saveRequest() {

        when(userService.getUserByIdIfExists(anyLong())).thenReturn(userForTests);

        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequestForTests);

        ItemRequestDto itemRequestDto = itemRequestService.saveRequest(1L, new ItemRequestDto());

        assertEquals(ItemRequestMapper.toItemRequestDto(itemRequestForTests), itemRequestDto);

    }

    @Test
    void getAllRequesterRequests() {

        List<ItemRequestRequesterDto> allRequests = List.of(ItemRequestMapper.toItemRequestRequesterDto(itemRequestForTests, List.of()));

        when(userService.getUserByIdIfExists(anyLong())).thenReturn(userForTests);
        when(itemRequestRepository.findAllByRequester(any(User.class))).thenReturn(List.of(itemRequestForTests));
        when(itemRepository.findAllByRequestInOrderByIdAsc(any())).thenReturn(List.of());

        List<ItemRequestRequesterDto> returnAllRequests = itemRequestService.getAllRequesterRequests(1L);

        assertEquals(allRequests.get(0), returnAllRequests.get(0));
    }

    @Test
    void getRequesterItemRequest() {

        when(userService.getUserByIdIfExists(anyLong())).thenReturn(userForTests);
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequestForTests));

        ItemRequestRequesterDto itemRequestRequesterDto = itemRequestService.getRequesterItemRequest(1L, 1L);

        assertEquals(ItemRequestMapper.toItemRequestRequesterDto(itemRequestForTests, List.of()), itemRequestRequesterDto);
    }

    @Test
    void getAllRequests() {

        when(userService.getUserByIdIfExists(anyLong())).thenReturn(userForTests);
        when(itemRequestRepository.findAllByRequesterNot(any(User.class), any())).thenReturn(List.of(itemRequestForTests));
        when(itemRepository.findAllByRequestInOrderByIdAsc(any())).thenReturn(List.of());

        List<ItemRequestRequesterDto> returnAllRequests = itemRequestService.getAllRequests(1L, 0, 10);

        assertEquals(1, returnAllRequests.size());
        assertEquals(ItemRequestMapper.toItemRequestRequesterDto(itemRequestForTests, List.of()), returnAllRequests.get(0));

    }

    @Test
    void getItemRequestIfExists() {
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequestForTests));

        ItemRequest itemRequest = itemRequestService.getItemRequestIfExists(1L);
    }
}