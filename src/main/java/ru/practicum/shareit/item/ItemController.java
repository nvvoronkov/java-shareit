package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemDto itemDto) {
        itemDto.setOwner(userId);
        return itemService.add(itemDto);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@PathVariable(value = "id") Long itemId, @RequestBody ItemDto itemDtoForUpdate,
                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        itemDtoForUpdate.setId(itemId);
        itemDtoForUpdate.setOwner(userId);
        return itemService.update(itemDtoForUpdate);
    }

    @GetMapping("/{id}")
    public ItemDto getById(@PathVariable("id") Long itemId) {
        return itemService.getById(itemId);
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAll(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@PathParam("text") String text) {
        return itemService.search(text);
    }
}
