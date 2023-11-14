package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getAllItemsByUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                                    @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("request received GET items/ | UserId - {} ", userId);
        return itemClient.getAllItems(userId, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> saveItem(@RequestHeader("X-Sharer-User-Id") long userId, @Validated @RequestBody ItemDto item) {
        log.info("request received POST items/ | UserId - {}", userId);
        return itemClient.save(item, userId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestBody ItemDto item,
                                             @PathVariable long id) {
        log.info("request received PATCH items/{id} | UserId - {}", userId);
        return itemClient.updateItem(userId, item, id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemInfo(@PathVariable long id, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("request received GET items/{id} | ItemId - {}", id);
        return itemClient.getItemById(id, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@PathParam("text") String text,
                                              @RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(value = "from", defaultValue = "0") int from,
                                              @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("request received /items/search text '{}'", text);
        return itemClient.searchItems(text, from, size, userId);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @PathVariable long itemId,
                                                @Valid @RequestBody CommentDto commentDto) {
        log.info("request received POST items/{itemId}/comment | UserId - {}", userId);
        return itemClient.createComment(userId, itemId, commentDto);
    }
}
