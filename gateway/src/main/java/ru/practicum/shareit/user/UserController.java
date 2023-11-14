package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> save(@Validated @RequestBody UserDto user) {
        log.info("request received GET /users | UserID - {}", user);
        return userClient.save(user);

    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("request received GET /users | UserID - {}", userClient);
        return userClient.getAllUsers();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@RequestBody UserDto user, @PathVariable long id) {
        log.info("request received GET /users/{id} | UserID - {}", id);
        return userClient.updateUser(user, id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable long id) {
        log.info("request received GET /users/{id} | UserID - {}", id);
        return userClient.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUserById(@PathVariable long id) {
        log.info("request received DELETE /users/{id} | UserID - {}", id);
        return userClient.deleteUserById(id);
    }
}
