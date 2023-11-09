package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRequestState;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingSaveDto;

import java.util.List;

@Service
public interface BookingService {

    BookingDto saveBooking(long userId, BookingSaveDto bookingSaveDto);

    BookingDto approvingBooking(long userId, boolean approved, long bookingId);

    BookingDto getBookingInfo(long bookingId, long userId);

    List<BookingDto> getBookingInfoList(long bookerId, BookingRequestState state, int from, int size);

    List<BookingDto> getBookingOwnerInfoList(long ownerId, BookingRequestState state, int from, int size);
}
