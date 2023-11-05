package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingSaveDto {

    @NotNull(message = "itemId не может быть пустым")
    private Long itemId;

    @NotNull(message = "начало бронирования не может быть пустым")
    private LocalDateTime start;

    @NotNull(message = "конец бронирования не может быть пустым")
    private LocalDateTime end;
}
