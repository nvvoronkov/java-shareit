package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserTest {
    private final User user1 = User.builder().id(1L).name("UserName1").email("User1@email.com").build();
    private final User user2 = new User(2L, "UserName2", "User2@email.com");
    private final User user3 = user1;

    @Test
    void testEquals() {
        Assertions.assertEquals(user1, user3);
        Assertions.assertNotEquals(user1, user2);
    }

    @Test
    void testHashCode() {
        Assertions.assertEquals(user1.hashCode(), user3.hashCode());
        Assertions.assertNotEquals(user1.hashCode(), user2.hashCode());
    }
}