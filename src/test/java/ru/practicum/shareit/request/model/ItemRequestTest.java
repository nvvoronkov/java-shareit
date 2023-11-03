package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

class ItemRequestTest {
    private final User user = new User(2L, "Nikita", "mail@email.ru");
    private final ItemRequest itemRequest1 = ItemRequest.builder().id(1L).description("Description")
            .created(LocalDateTime.now()).build();
    private final ItemRequest itemRequest2 = new ItemRequest(2L, "Description", user,
            LocalDateTime.now());
    private final ItemRequest itemRequest3 = itemRequest1;

    @Test
    void testEquals() {
        Assertions.assertEquals(itemRequest1, itemRequest3);
        Assertions.assertNotEquals(itemRequest1, itemRequest2);
    }

    @Test
    void testHashCode() {
        Assertions.assertEquals(itemRequest1.hashCode(), itemRequest3.hashCode());
        Assertions.assertNotEquals(itemRequest1.hashCode(), itemRequest2.hashCode());
    }
}