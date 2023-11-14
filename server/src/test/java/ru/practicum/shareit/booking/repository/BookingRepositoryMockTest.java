package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingRepositoryMockTest {
    private final User userForTests = new User(null, "test", "test@gmail.com");

    private final Item itemForTests = new Item(null, "test", "test", true, userForTests, null);
    private final Booking bookingForTests = new Booking(null, LocalDateTime.now().plusHours(24), LocalDateTime.now(), itemForTests, userForTests, BookingStatus.WAITING);
    private final User userForTests2 = new User(null, "test2", "test2@gmail.com");
    private final Item itemForTests2 = new Item(null, "test2", "test2", true, userForTests2, null);
    private final Booking bookingForTests2 = new Booking(null, LocalDateTime.now(), LocalDateTime.now().minusDays(2), itemForTests2, userForTests2, BookingStatus.APPROVED);
    @Mock
    private BookingRepository bookingRepository;

    public BookingRepositoryMockTest() {
    }

    @Test
    public void testFindAllByBookerForFuture() {
        User booker = new User();
        LocalDateTime currentTime = LocalDateTime.now();
        PageRequest pageable = PageRequest.of(0, 10);

        List<Booking> bookings = new ArrayList<>();
        when(bookingRepository.findAllByBookerForFuture(eq(booker), eq(currentTime), eq(pageable)))
                .thenReturn(bookings);

        List<Booking> result = bookingRepository.findAllByBookerForFuture(booker, currentTime, pageable);

        assertEquals(bookings, result);
    }

    @Test
    public void testFindAllByBookerOrderByStartDesc() {
        PageRequest pageable = PageRequest.of(0, 10);

        List<Booking> bookings = new ArrayList<>();
        bookings.add(bookingForTests);
        when(bookingRepository.findAllByBookerOrderByStartDesc(eq(userForTests), eq(pageable)))
                .thenReturn(bookings);

        List<Booking> result = bookingRepository.findAllByBookerOrderByStartDesc(userForTests, pageable);

        assertEquals(bookings, result);
    }

    @Test
    public void testFindAllByItemAndStatusOrderByStartAsc() {
        BookingStatus status = BookingStatus.APPROVED;

        List<Booking> bookings = new ArrayList<>();
        bookings.add(bookingForTests2);
        when(bookingRepository.findAllByItemAndStatusOrderByStartAsc(eq(itemForTests), eq(status)))
                .thenReturn(bookings);

        List<Booking> result = bookingRepository.findAllByItemAndStatusOrderByStartAsc(itemForTests, status);

        assertEquals(bookings, result);
    }

    @Test
    public void testFindAllByBookerAndItemAndStatus() {
        LocalDateTime currentTime = LocalDateTime.now();

        List<Booking> bookings = new ArrayList<>();
        bookings.add(bookingForTests);
        when(bookingRepository.findAllByBookerAndItemAndStatus(eq(userForTests), eq(itemForTests), eq(currentTime)))
                .thenReturn(bookings);

        List<Booking> result = bookingRepository.findAllByBookerAndItemAndStatus(userForTests, itemForTests, currentTime);

        assertEquals(bookings, result);
    }
}
