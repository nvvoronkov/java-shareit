package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingSaveDto;

import java.util.List;

public interface BookingService {
    BookingDto saveBooking(long userId, BookingSaveDto bookingSaveDto);

    BookingDto approvingBooking(long userId, boolean approved, long bookingId);

    BookingDto getBookingInfo(long bookingId, long userId);

    List<BookingDto> getBookingInfoList(long bookerId, String state);

    List<BookingDto> getBookingOwnerInfoList(long ownerId, String state);
}
