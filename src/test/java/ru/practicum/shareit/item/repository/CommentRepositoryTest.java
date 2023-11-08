package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private final User userForTests = new User(null, "test", "test@gmail.com");

    private final Item itemForTests = new Item(null, "test", "test", true, userForTests, null);

    private final Comment commentForTests = new Comment(null, "test", itemForTests, userForTests, LocalDateTime.now());

    @BeforeEach
    void setUp() {
        userRepository.save(userForTests);
        itemRepository.save(itemForTests);
        commentRepository.save(commentForTests);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    void findAllByItemIn() {
        List<Comment> comments = commentRepository.findAllByItemIn(List.of(itemForTests));

        assertEquals(1, comments.size());
        assertEquals(commentForTests, comments.get(0));

    }

    @Test
    void findAllByItem() {
        List<Comment> comments = commentRepository.findAllByItem(itemForTests);

        assertEquals(1, comments.size());
        assertEquals(commentForTests, comments.get(0));
    }
}