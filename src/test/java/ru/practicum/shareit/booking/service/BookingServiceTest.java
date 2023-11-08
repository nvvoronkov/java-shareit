package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingSaveDto;
import ru.practicum.shareit.booking.enums.BookingRequestState;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemService itemService;

    @Mock
    private UserService userService;

    @InjectMocks
    private BookingService bookingService;

    private final User ownerForTests = new User(2L, "test", "test");

    private final User bookerForTests = new User(1L, "test2", "test2");

    private final Item itemForTests = new Item(1L, "test", "test", true, ownerForTests, null);

    private final BookingSaveDto bookingSaveDto = new BookingSaveDto(1L, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(6));

    private final Booking bookingForTests = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(24), itemForTests, bookerForTests, BookingStatus.WAITING);

    @Test
    void saveBooking_whenSuccess() {
        when(itemService.getItemIfExists(anyLong())).thenReturn(itemForTests);

        when(userService.getUserByIdIfExists(anyLong())).thenReturn(ownerForTests);

        when(bookingRepository.save(any(Booking.class))).thenReturn(bookingForTests);

        BookingDto bookingDto = bookingService.saveBooking(1L, bookingSaveDto);

        assertEquals(BookingMapper.toBookingDto(bookingForTests), bookingDto);
        verify(bookingRepository, times(1)).save(any(Booking.class));
        verify(itemService, times(1)).getItemIfExists(anyLong());
        verify(userService, times(1)).getUserByIdIfExists(anyLong());


    }

    @Test
    void approvingBooking_whenSuccess() {

        Booking bookingWaiting = new Booking(1L,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(24),
                itemForTests,
                bookerForTests,
                BookingStatus.WAITING);

        Booking bookingApproved = new Booking(1L,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(24),
                itemForTests,
                bookerForTests,
                BookingStatus.APPROVED);

        when(userService.getUserByIdIfExists(anyLong())).thenReturn(new User());
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(bookingWaiting));
        when(bookingRepository.save(bookingWaiting)).thenReturn(bookingApproved);

        BookingDto bookingDto = bookingService.approvingBooking(2L, true, 1L);

        assertEquals(BookingMapper.toBookingDto(bookingApproved), bookingDto);
        assertEquals(bookingDto.getStatus(), BookingStatus.APPROVED);

    }

    @Test
    void getBookingInfo() {

        when(userService.getUserByIdIfExists(anyLong())).thenReturn(new User());
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(bookingForTests));

        BookingDto bookingDto = bookingService.getBookingInfo(1L, 2L);

        assertEquals(BookingMapper.toBookingDto(bookingForTests), bookingDto);

    }

    @Test
    void getBookingInfoList() {

        List<Booking> toReturn = List.of(bookingForTests);

        when(userService.getUserByIdIfExists(anyLong())).thenReturn(bookerForTests);
        when(bookingRepository.findAllByBookerOrderByStartDesc(bookerForTests, PageRequest.of(0, 10))).thenReturn(toReturn);

        List<BookingDto> result = bookingService.getBookingInfoList(bookerForTests.getId(), String.valueOf(BookingRequestState.ALL), 0, 10);

        assertEquals(BookingMapper.toBookingDto(toReturn.get(0)), result.get(0));
    }

    @Test
    void getBookingOwnerInfoList() {
        List<Booking> toReturn = List.of(bookingForTests);

        when(userService.getUserByIdIfExists(anyLong())).thenReturn(ownerForTests);
        when(bookingRepository.findAllByOwnerOrderByStartDesc(ownerForTests, PageRequest.of(0, 10))).thenReturn(toReturn);

        List<BookingDto> result = bookingService.getBookingOwnerInfoList(bookerForTests.getId(), String.valueOf(BookingRequestState.ALL), 0, 10);

        assertEquals(BookingMapper.toBookingDto(toReturn.get(0)), result.get(0));
    }
}
