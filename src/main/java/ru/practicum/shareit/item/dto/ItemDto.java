package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto {

    private Long id;

    @NotNull(message = "имя вещи не может быть пустым")
    @NotBlank(message = "имя вещи не может быть пустым")
    private String name;

    @NotNull(message = "описание вещи не может быть пустым")
    @NotBlank(message = "описание вещи не может быть пустым")
    private String description;

    @NotNull(message = "поле доступа вещи не может быть пустым")
    private Boolean available;

    private Long requestId;
}
