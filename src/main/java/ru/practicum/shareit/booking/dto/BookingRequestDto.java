package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.enums.BookingStatus;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequestDto {
    private Long id;
    @NotBlank(message = "В запросе не предостален id пользователя")
    private Long bookerId;
    private Long itemId;
    private String start;
    private String end;
    private BookingStatus status;
}
