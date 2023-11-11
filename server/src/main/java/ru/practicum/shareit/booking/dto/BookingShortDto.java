package ru.practicum.shareit.booking.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingShortDto {
    Long id;
    Long bookerId;
}

