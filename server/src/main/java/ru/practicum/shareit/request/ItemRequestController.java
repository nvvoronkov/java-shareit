package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestRequesterDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;


@RestController
@RequestMapping(path = "/requests")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping()
    public ItemRequestDto saveRequest(@RequestHeader("X-Sharer-User-Id") long requesterId,
                                      @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("получен запрос: POST /requests");
        return itemRequestService.saveRequest(requesterId, itemRequestDto);
    }

    @GetMapping()
    public List<ItemRequestRequesterDto> getAllRequesterRequests(@RequestHeader("X-Sharer-User-Id") long requesterId
    ) {
        log.info("получен запрос: GET /requests");
        return itemRequestService.getAllRequesterRequests(requesterId);
    }

    @GetMapping("/all")
    public List<ItemRequestRequesterDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") long requesterId,
                                                        @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
                                                        @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
        log.info("получен запрос: GET /requests/all");
        return itemRequestService.getAllRequests(requesterId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestRequesterDto getRequesterItemRequest(@RequestHeader("X-Sharer-User-Id") long requesterId,
                                                           @PathVariable long requestId) {
        log.info("получен запрос: GET /requests/{requestId}");
        return itemRequestService.getRequesterItemRequest(requesterId, requestId);
    }
}
