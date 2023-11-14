package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemReturnDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    private final User userForTests = new User(1L, "test", "test");
    private final ItemRequest itemRequestForTests = new ItemRequest(1L, "test", userForTests, LocalDateTime.now());
    private final Item itemForTests = new Item(1L, "test", "test", true, userForTests, itemRequestForTests);
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserService userService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void saveItem_whenSuccess() {
        Item itemSaveTest = new Item();
        itemSaveTest.setName("test");
        itemSaveTest.setDescription("test");
        itemSaveTest.setAvailable(true);


        when(userService.getUserByIdIfExists(userForTests.getId())).thenReturn(userForTests);
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ItemDto actualItemDto = itemService.saveItem(userForTests.getId(), ItemMapper.toItemDto(itemSaveTest));

        assertEquals(actualItemDto.getName(), "test");
        assertEquals(actualItemDto.getDescription(), "test");

    }

    @Test
    void updateItem() {

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemForTests));


        ItemDto updateData = new ItemDto();
        updateData.setName("updatedName");
        updateData.setDescription("updatedDescription");

        Item toUpdate = itemForTests;
        toUpdate.setName("updatedName");
        toUpdate.setDescription("updatedDescription");

        when(itemRepository.save(toUpdate)).thenReturn(toUpdate);

        ItemDto afterUpdate = itemService.updateItem(userForTests.getId(), updateData, itemForTests.getId());

        assertEquals(afterUpdate.getName(), "updatedName");
        assertEquals(afterUpdate.getDescription(), "updatedDescription");
    }

    @Test
    void getItemInfo() {

        when(commentRepository.findAllByItem(any(Item.class))).thenReturn(List.of());
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemForTests));
        when(bookingRepository.findAllByItemAndStatusOrderByStartAsc(any(Item.class), any(BookingStatus.class))).thenReturn(List.of());

        ItemReturnDto actualItemDto = itemService.getItemInfo(itemForTests.getId(), userForTests.getId());

        assertEquals(actualItemDto, ItemMapper.toItemReturnDto(itemForTests, null, null, List.of()));
    }

    @Test
    void searchItems() {
        when(itemRepository.findAllByNameAndDescription(anyString(), any())).thenReturn(List.of(itemForTests));

        List<ItemDto> actualItemsDto = itemService.searchItems("test", 0, 10);

        assertEquals(actualItemsDto.size(), 1);
        assertEquals(actualItemsDto.get(0).getName(), "test");
        assertEquals(actualItemsDto.get(0).getDescription(), "test");

    }
}