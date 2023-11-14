package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingSaveDto {
    @NotNull(message = "itemId cannot be empty")
    private Long itemId;

    @NotNull(message = "booking start cannot be empty")
    private LocalDateTime start;

    @NotNull(message = "booking end cannot be empty")
    private LocalDateTime end;
}
