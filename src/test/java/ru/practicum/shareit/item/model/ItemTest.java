package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

class ItemTest {
    private final Item item1 = Item.builder().id(1L).name("Name 1").description("Description 1").owner(new User())
            .available(true).build();
    private final Item item2 = new Item(2L, "Name 2", "Description 2", true, new User(),
            new ItemRequest());
    private final Item item3 = item1;

    @Test
    void testEquals() {
        Assertions.assertEquals(item1, item3);
        Assertions.assertNotEquals(item1, item2);
    }

    @Test
    void testHashCode() {
        Assertions.assertEquals(item1.hashCode(), item3.hashCode());
        Assertions.assertNotEquals(item1.hashCode(), item2.hashCode());
    }
}