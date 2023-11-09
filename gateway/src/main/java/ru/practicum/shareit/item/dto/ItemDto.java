package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id;

    @NotNull(message = "item name cannot be empty")
    @NotBlank(message = "item name cannot be empty")
    private String name;

    @NotNull(message = "item description cannot be empty")
    @NotBlank(message = "item description cannot be empty")
    private String description;

    @NotNull(message = "a thing's access field cannot be empty")
    private Boolean available;

    private Long requestId;
}
