package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

class BookingTest {
    private final User owner = User.builder().id(2L).name("owner").email("owner@email.com").build();
    private final User user = User.builder().id(1L).name("UserName1").email("User1@email.com").build();
    private final Item item = Item.builder().id(1L).name("Name 1").description("Description 1").owner(owner)
            .available(true).build();
    private final Booking booking1 = Booking.builder().id(1L).item(item).booker(user)
            .start(LocalDateTime.parse("2023-11-03T14:03:50"))
            .end(LocalDateTime.parse("2024-11-03T14:03:50"))
            .status(BookingStatus.WAITING).build();
    private final Booking booking3 = booking1;
    private final Booking booking2 = new Booking(2L, LocalDateTime.parse("2023-11-03T14:03:50"),
            LocalDateTime.parse("2024-11-03T14:03:50"), item, user, BookingStatus.WAITING);

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