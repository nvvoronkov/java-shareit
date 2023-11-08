package ru.practicum.shareit.booking.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingShortDto {
    Long id;
    Long bookerId;
}

