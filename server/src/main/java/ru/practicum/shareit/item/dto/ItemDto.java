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

    @NotNull(message = "name cannot be empty")
    @NotBlank(message = "name cannot be empty")
    private String name;

    @NotNull(message = "description cannot be empty")
    @NotBlank(message = "description cannot be empty")
    private String description;

    @NotNull(message = "available cannot be empty")
    private Boolean available;

    private Long requestId;
}
