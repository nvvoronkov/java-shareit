package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingSaveDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        ItemDto itemDto = ItemMapper.toItemDto(booking.getItem());
        UserDto bookerForReturn = UserMapper.toUserDto(booking.getBooker());

        return new BookingDto(booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                itemDto,
                booking.getStatus(),
                bookerForReturn
        );
    }

    public static BookingShortDto toBookingShortDto(Booking booking) {
        return new BookingShortDto(booking.getId(), booking.getBooker().getId());
    }

    public static Booking toSaveBooking(BookingSaveDto bookingSaveDtoDto, User booker, Item item) {
        return new Booking(null,
                bookingSaveDtoDto.getStart(),
                bookingSaveDtoDto.getEnd(),
                item,
                booker,
                BookingStatus.WAITING);
    }
}
