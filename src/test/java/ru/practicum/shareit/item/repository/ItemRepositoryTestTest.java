package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;

@DataJpaTest(properties = "spring.sql.init.data-locations=data-test.sql")
public class ItemRepositoryTestTest {

    @Autowired
    ItemRequestRepository itemRequestRepository;

    @Test
    void shouldAddIdWhenSaveNewEntity() {
        ItemRequest itemRequest = ItemRequest.builder().description("ItemRequest description")
                .created(LocalDateTime.now()).build();
        Assertions.assertNull(itemRequest.getId());
        itemRequestRepository.save(itemRequest);
        Assertions.assertNotNull(itemRequest.getId());
    }

    @Test
    void shouldThrowExceptionWhenSaveNullRequestor() {
        ItemRequest itemRequest = ItemRequest.builder().description("ItemRequest description")
                .created(LocalDateTime.now()).build();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> itemRequestRepository.save(itemRequest));
    }

    @Test
    void shouldThrowExceptionWhenSaveNullDescription() {
        ItemRequest itemRequest = ItemRequest.builder().created(LocalDateTime.now()).build();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> itemRequestRepository.save(itemRequest));
    }

    @Test
    void shouldThrowExceptionWhenSaveNullCreated() {
        ItemRequest itemRequest = ItemRequest.builder().description("ItemRequest description").build();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> itemRequestRepository.save(itemRequest));
    }
}
