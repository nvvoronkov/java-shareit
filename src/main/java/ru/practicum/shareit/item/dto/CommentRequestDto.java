package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {
    private Long id;
    @NotNull(message = "Текст комментария не должен быть пустым")
    private String text;
    @NotNull(message = "В запросе не предостален id объекта")
    private Long item;
    @NotNull(message = "В запросе не предостален id пользователя")
    private Long author;
}
