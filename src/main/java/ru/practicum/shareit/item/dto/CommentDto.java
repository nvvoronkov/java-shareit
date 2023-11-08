package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    @NotNull(message = "комментарий не может быть пустым")
    @NotEmpty(message = "комментарий не может быть пустым")
    private String text;
}
