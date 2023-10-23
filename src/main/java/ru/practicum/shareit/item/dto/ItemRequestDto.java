package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    private Long id;
    private String name;
    private String description;
    @NotBlank(message = "В запросе не предостален id пользователя")
    private Long owner;
    private Boolean available;
}

