package ru.practicum.shareit.booking.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
@AllArgsConstructor
public class BookingMapper {
    private static final DateTimeFormatter DATE_TIME_PATTERN = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final UserService userService;
    private final ItemService itemService;

    public BookingResponseDto entityToResponseDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        return BookingResponseDto.builder()
                .id(booking.getId())
                .booker(userService.getById(booking.getBookerId()))
                .item(itemService.findById(booking.getItemId()))
                .start(instantToString(booking.getStart()))
                .end(instantToString(booking.getEnd()))
                .status(booking.getStatus())
                .build();
    }

    public Booking requestDtoToEntity(BookingRequestDto bookingRequestDto) {
        if (bookingRequestDto == null) {
            return null;
        }
        return Booking.builder()
                .bookerId(bookingRequestDto.getBookerId())
                .itemId(bookingRequestDto.getItemId())
                .start(stringToInstant(bookingRequestDto.getStart()))
                .end(stringToInstant(bookingRequestDto.getEnd()))
                .status(bookingRequestDto.getStatus())
                .build();
    }

    private String instantToString(Instant instantTime) {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instantTime, ZoneId.systemDefault());
        return DATE_TIME_PATTERN.withZone(ZoneId.systemDefault()).format(zonedDateTime);
    }

    private Instant stringToInstant(String stringTime) {
        LocalDateTime localDateTime = LocalDateTime.parse(stringTime, DATE_TIME_PATTERN);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        return zonedDateTime.toInstant();
    }
}