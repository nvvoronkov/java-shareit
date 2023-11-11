package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

class CommentTest {
    User user = new User(2L, "Nikita", "mail@email.ru");
    Item item = new Item(3L, "рулетка", "простая рулетка", true, user, new ItemRequest());
    private final Comment comment1 = Comment.builder().id(1L).text("Text").author(user).created(LocalDateTime.now()).item(item)
            .build();
    private final Comment comment3 = comment1;
    private final Comment comment2 = new Comment(2L, "Text", item, user, LocalDateTime.now());

    @Test
    void testEquals() {
        Assertions.assertEquals(comment1, comment3);
        Assertions.assertNotEquals(comment1, comment2);
    }

    @Test
    void testHashCode() {
        Assertions.assertEquals(comment1.hashCode(), comment3.hashCode());
        Assertions.assertNotEquals(comment1.hashCode(), comment2.hashCode());
    }
}