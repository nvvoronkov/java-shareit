package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BookingRepositoryTest {

    private final User userForTests = new User(null, "test", "test@gmail.com");
    private final Item itemForTests = new Item(null, "test", "test", true, userForTests, null);
    private final Booking bookingForTests = new Booking(null, LocalDateTime.now().plusHours(24), LocalDateTime.now(), itemForTests, userForTests, BookingStatus.WAITING);
    private final User userForTests2 = new User(null, "test2", "test2@gmail.com");
    private final Item itemForTests2 = new Item(null, "test2", "test2", true, userForTests2, null);
    private final Booking bookingForTests2 = new Booking(null, LocalDateTime.now(), LocalDateTime.now().minusDays(2), itemForTests2, userForTests2, BookingStatus.APPROVED);
    private final Booking bookingForTests3 = new Booking(null, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2), itemForTests2, userForTests2, BookingStatus.APPROVED);
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(userForTests);
        userRepository.save(userForTests2);
        itemRepository.save(itemForTests);
        itemRepository.save(itemForTests2);
        bookingRepository.save(bookingForTests);
        bookingRepository.save(bookingForTests2);
        bookingRepository.save(bookingForTests3);

    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        bookingRepository.deleteAll();
    }


    @Test
    void findAllByBookerForFuture() {
        List<Booking> all = bookingRepository.findAllByBookerForFuture(userForTests, LocalDateTime.now(), PageRequest.of(0, 10));
        assertEquals(1, all.size());
        assertEquals(userForTests, all.get(0).getBooker());
    }

    @Test
    void findAllByBookerOrderByStartDesc() {
        List<Booking> all = bookingRepository.findAllByBookerOrderByStartDesc(userForTests, PageRequest.of(0, 10));
        assertEquals(1, all.size());
        assertEquals(userForTests, all.get(0).getBooker());
    }

    @Test
    void findAllByBookerAndStatusOrderByStartDesc() {
        List<Booking> all = bookingRepository.findAllByBookerAndStatusOrderByStartDesc(userForTests, BookingStatus.WAITING, PageRequest.of(0, 10));
        assertEquals(1, all.size());
        assertEquals(userForTests, all.get(0).getBooker());
    }

    @Test
    void findAllByOwnerForFuture() {
        List<Booking> all = bookingRepository.findAllByOwnerForFuture(userForTests, LocalDateTime.now(), PageRequest.of(0, 10));
        assertEquals(1, all.size());
        assertEquals(userForTests, all.get(0).getBooker());
    }

    @Test
    void findAllByOwnerOrderByStartDesc() {
        List<Booking> all = bookingRepository.findAllByOwnerOrderByStartDesc(userForTests, PageRequest.of(0, 10));
        assertEquals(1, all.size());
        assertEquals(userForTests, all.get(0).getBooker());
    }

    @Test
    void findAllByOwnerAndStatusOrderByStartDesc() {
        List<Booking> all = bookingRepository.findAllByOwnerForFuture(userForTests, LocalDateTime.now(), PageRequest.of(0, 10));
        assertEquals(1, all.size());
        assertEquals(userForTests, all.get(0).getBooker());
    }

    @Test
    void findAllByItemInAndStatusOrderByStartAsc() {
        List<Booking> all = bookingRepository.findAllByItemInAndStatusOrderByStartAsc(List.of(itemForTests), BookingStatus.WAITING);
        assertEquals(1, all.size());
        assertEquals(userForTests, all.get(0).getBooker());
    }

    @Test
    void findAllByItemAndStatusOrderByStartAsc() {
        List<Booking> all = bookingRepository.findAllByItemAndStatusOrderByStartAsc(itemForTests, BookingStatus.WAITING);
        assertEquals(1, all.size());
        assertEquals(userForTests, all.get(0).getBooker());
    }

    @Test
    void findAllByBookerAndItemAndStatus() {
        List<Booking> all = bookingRepository.findAllByBookerAndItemAndStatus(userForTests2, itemForTests2, LocalDateTime.now());
        assertEquals(1, all.size());
        assertEquals(userForTests2, all.get(0).getBooker());
    }

    @Test
    void findAllCurrentBookingsByBooker() {
        List<Booking> all = bookingRepository.findAllCurrentBookingsByBooker(userForTests2, LocalDateTime.now(), PageRequest.of(0, 10));
        assertEquals(1, all.size());
        assertEquals(userForTests2, all.get(0).getBooker());
    }

    @Test
    void findAllCurrentBookingsByOwner() {
        List<Booking> all = bookingRepository.findAllCurrentBookingsByOwner(userForTests2, LocalDateTime.now(), PageRequest.of(0, 10));
        assertEquals(1, all.size());
        assertEquals(userForTests2, all.get(0).getBooker());
    }

    @Test
    void findAllPastBookingsByBooker() {
        List<Booking> all = bookingRepository.findAllPastBookingsByBooker(userForTests2, LocalDateTime.now(), PageRequest.of(0, 10));
        assertEquals(1, all.size());
        assertEquals(userForTests2, all.get(0).getBooker());
    }

    @Test
    void findAllPastBookingsByOwner() {
        List<Booking> all = bookingRepository.findAllPastBookingsByOwner(userForTests2, LocalDateTime.now(), PageRequest.of(0, 10));
        assertEquals(1, all.size());
        assertEquals(userForTests2, all.get(0).getBooker());
    }
}