package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

class BookingTest {
    private final User owner = User.builder().id(2L).name("owner").email("owner@email.com").build();
    private final User user = User.builder().id(1L).name("UserName1").email("User1@email.com").build();
    private final Item item = Item.builder().id(1L).name("Name 1").description("Description 1").owner(owner)
            .available(true).build();
    private final Booking booking1 = Booking.builder().id(1L).item(item).booker(user)
            .start(LocalDateTime.parse("2021-10-10T10:10:10.00Z"))
            .end(LocalDateTime.parse("2020-11-10T10:10:10.00Z"))
            .status(BookingStatus.WAITING).build();
    private final Booking booking2 = new Booking(2L, LocalDateTime.parse("2021-10-10T10:10:10.00Z"),
            LocalDateTime.parse("2021-11-10T10:10:10.00Z"), item, user, BookingStatus.WAITING);
    private final Booking booking3 = booking1;

    @Test
    void testEquals() {
        Assertions.assertEquals(booking1, booking3);
        Assertions.assertNotEquals(booking1, booking2);
    }

    @Test
    void testHashCode() {
        Assertions.assertEquals(booking1.hashCode(), booking3.hashCode());
        Assertions.assertNotEquals(booking1.hashCode(), booking2.hashCode());
    }
}