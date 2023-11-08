package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ItemRequestRepositoryTest {

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    private final User userForTests = new User(null, "test", "test@gmail.com");

    private final User userForTests2 = new User(null, "test", "test@gmail.com");
    private final ItemRequest itemRequestForTests = new ItemRequest(null, "test", userForTests, LocalDateTime.now());

    private final ItemRequest itemRequestForTests2 = new ItemRequest(null, "test", userForTests2, LocalDateTime.now());

    @BeforeEach
    void setUp() {

        userRepository.save(userForTests);
        userRepository.save(userForTests2);
        itemRequestRepository.save(itemRequestForTests);
        itemRequestRepository.save(itemRequestForTests2);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        itemRequestRepository.deleteAll();
    }

    @Test
    void findAllByRequester() {
        List<ItemRequest> all = itemRequestRepository.findAllByRequester(userForTests);
        assertEquals(1, all.size());
        assertEquals(itemRequestForTests, all.get(0));
    }

    @Test
    void findById() {
        Optional<ItemRequest> actualItemRequest = itemRequestRepository.findById(itemRequestForTests2.getId());
        assertTrue(actualItemRequest.isPresent());

    }

    @Test
    void findAllByRequesterNot() {
        List<ItemRequest> all = itemRequestRepository.findAllByRequesterNot(userForTests, PageRequest.of(0, 10));
        assertEquals(1, all.size());
        assertEquals(itemRequestForTests2, all.get(0));
    }
}