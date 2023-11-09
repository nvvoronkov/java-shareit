package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentReturnDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemReturnDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemReturnDto> getAllItemsByUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                                 @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
        log.info("request received GET items/ | UserId - {} ", userId);
        return itemService.getAllItemsByUser(userId, from, size);
    }

    @PostMapping
    public ItemDto saveItem(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody ItemDto item) {
        log.info("request received POST items/ | UserId - {}", userId);
        return itemService.saveItem(userId, item);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @RequestBody ItemDto item,
                              @PathVariable long id) {
        log.info("request received PATCH items/{id} | UserId - {}", userId);
        return itemService.updateItem(userId, item, id);
    }

    @GetMapping("/{id}")
    public ItemReturnDto getItemInfo(@PathVariable long id, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("request received GET items/{id} | ItemId - {}", id);
        return itemService.getItemInfo(id, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@PathParam("text") String text,
                                     @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
                                     @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
        log.info("request received GET /items/search text '{}'", text);
        return itemService.searchItems(text, from, size);
    }

    @PostMapping("{itemId}/comment")
    public CommentReturnDto createComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @PathVariable long itemId,
                                          @Valid @RequestBody CommentDto commentDto) {
        log.info("request received POST items/{itemId}/comment | UserId - {}", userId);
        return itemService.createComment(userId, itemId, commentDto);
    }
}
