package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ItemRequestRepository itemRequestRepository;


    private final User userForTests = new User(null, "test", "test@gmail.com");

    private final Item itemForTests = new Item(null, "test", "test", true, userForTests, null);

    private final User userForTests2 = new User(null, "test2", "test2@gmail.com");

    private final ItemRequest itemRequestForTests = new ItemRequest(null, "test", userForTests, LocalDateTime.now());

    private final Item itemForTests2 = new Item(null, "test2", "test2", true, userForTests2, itemRequestForTests);

    @BeforeEach
    void setUp() {
        userRepository.save(userForTests);
        userRepository.save(userForTests2);
        itemRepository.save(itemForTests);
        itemRepository.save(itemForTests2);
        itemRequestRepository.save(itemRequestForTests);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        itemRequestRepository.deleteAll();
    }

    @Test
    void findAllByOwnerId() {
        List<Item> all = itemRepository.findAllByOwnerId(userForTests.getId(), PageRequest.of(0, 10));

        assertEquals(1, all.size());
        assertEquals(itemForTests, all.get(0));
    }

    @Test
    void findAllByNameAndDescription() {

        List<Item> all = itemRepository.findAllByNameAndDescription("test", PageRequest.of(0, 10));

        assertEquals(2, all.size());
        assertEquals(itemForTests, all.get(0));
    }

    @Test
    void findAllByRequestInOrderByIdAsc() {
        List<Item> all = itemRepository.findAllByRequestInOrderByIdAsc(List.of(itemRequestForTests));

        assertEquals(1, all.size());
        assertEquals(itemForTests2, all.get(0));
    }

    @Test
    void findAllByRequest() {
        List<Item> all = itemRepository.findAllByRequest(itemRequestForTests);

        assertEquals(1, all.size());
        assertEquals(itemForTests2, all.get(0));
    }
}