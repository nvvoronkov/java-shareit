package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemResponseResponseDto add(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @RequestBody ItemRequestDto itemRequestDto) {
        itemRequestDto.setOwner(userId);
        return itemService.add(itemRequestDto);
    }

    @PatchMapping("/{id}")
    public ItemResponseResponseDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable(value = "id") Long itemId,
                                          @RequestBody ItemRequestDto itemRequestDto) {
        itemRequestDto.setId(itemId);
        itemRequestDto.setOwner(userId);
        return itemService.update(itemRequestDto);
    }

    @GetMapping("/{id}")
    public ResponseDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @PathVariable("id") Long itemId) {
        return itemService.getById(userId, itemId);
    }

    @GetMapping
    public List<ItemResponseResponseDtoForOwner> findAllByOwner(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAllByOwner(userId);
    }

    @GetMapping("/search")
    public List<ItemResponseResponseDto> search(@PathParam("text") String text) {
        return itemService.search(text);
    }

    @PostMapping("{itemId}/comment")
    public CommentResponseDto addComment(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                         @PathVariable(value = "itemId") Long itemId,
                                         @RequestBody String text) {
        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .author(userId)
                .item(itemId)
                .text(text.substring(15, (text.length() - 3)))
                .build();
        return itemService.addComment(commentRequestDto);
    }
}