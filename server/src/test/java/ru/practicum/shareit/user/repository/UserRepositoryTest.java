package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@DataJpaTest
class UserRepositoryTest {
    final String name = "UserName";
    final String email = "User@mail.com";
    User user;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        user = User.builder().name(name).email(email).build();
    }

    @Test
    void shouldAddIdWhenSaveNewEntity() {
        Assertions.assertNull(user.getId());
        userRepository.save(user);
        Assertions.assertNotNull(user.getId());
    }

    @Test
    void shouldGetById() {
        userRepository.save(user);
        User result = userRepository.findById(user.getId()).get();
        Assertions.assertEquals(user.getId(), result.getId());
    }

    @Test
    void shouldGetAllEntity() {
        User user2 = User.builder().name("UserName2").email("User2@mail.com").build();
        userRepository.save(user);
        userRepository.save(user2);
        List<User> result = userRepository.findAll();
        Assertions.assertEquals(2, result.size());
    }
}