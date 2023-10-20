package ru.practicum.shareit.request.model;

import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class ItemRequest {
    private Long id;
    private String description;
    private User requester;
    private LocalDateTime created;
}
