package ru.practicum.shareit.request;

import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

public class ItemRequest {
    private Long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
}
