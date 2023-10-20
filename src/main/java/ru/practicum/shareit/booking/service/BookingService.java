package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto add(BookingRequestDto bookingRequestDto);

    BookingResponseDto update(Long userId, Long bookingId, Boolean isApproved);

    BookingResponseDto getById(Long userId, Long bookingId);

    List<BookingResponseDto> getAllBookingsByBookerId(Long userId, String state);

    List<BookingResponseDto> getAllBookingsByOwnerItems(Long userId, String state);
}
