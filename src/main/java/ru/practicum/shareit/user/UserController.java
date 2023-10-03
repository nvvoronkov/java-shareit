package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.interfaces.Services;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {
    private Services<UserDto> services;

    @PostMapping
    public UserDto add(@RequestBody @Valid UserDto userDto) {
        return services.add(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable(value = "id") Long userId, @RequestBody UserDto userDtoForUpdate) {
        userDtoForUpdate.setId(userId);
        return services.update(userDtoForUpdate);
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable(value = "id") Long userId) {
        return services.getById(userId);
    }

    @GetMapping
    public List<UserDto> getAll() {
        return services.getAll();
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable(value = "id") Long userId) {
        return services.delete(userId);
    }
}
