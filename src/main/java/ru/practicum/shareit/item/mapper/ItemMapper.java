package ru.practicum.shareit.item.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repositiory.BookingRepository;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseResponseDtoForOwner;
import ru.practicum.shareit.item.model.Item;

import java.time.Instant;
import java.util.List;

@Component
@Validated
@AllArgsConstructor
public class ItemMapper {
    private final BookingRepository bookingRepository;

    public ItemResponseResponseDto toDtoForOtherUsers(Item item, List<CommentResponseDto> listOfComments) {
        if (item == null) {
            return null;
        }
        return ItemResponseResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(item.getRequest())
                .lastBooking(null)
                .nextBooking(null)
                .comments(listOfComments)
                .build();
    }

    public ItemResponseResponseDtoForOwner toDtoForOwner(Item item, List<CommentResponseDto> listOfComments) {
        if (item == null) {
            return null;
        }
        return ItemResponseResponseDtoForOwner.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(item.getRequest())
                .lastBooking(findLastBookingsByItemId(item.getId()))
                .nextBooking(findNextBookingsByItemId(item.getId()))
                .comments(listOfComments)
                .build();
    }

    public Item requestDtoToEntity(ItemRequestDto itemRequestDto) {
        if (itemRequestDto == null) {
            return null;
        }
        return Item.builder()
                .id(itemRequestDto.getId())
                .name(itemRequestDto.getName())
                .description(itemRequestDto.getDescription())
                .owner(itemRequestDto.getOwner())
                .available(itemRequestDto.getAvailable())
                .build();
    }

    public Booking findLastBookingsByItemId(Long itemId) {
        List<Booking> listOfBookings = bookingRepository.findAllByItemIdAndEndBeforeOrderByEndDesc(itemId, Instant.now());
        return listOfBookings.isEmpty() ? null : listOfBookings.get(0);
    }

    public Booking findNextBookingsByItemId(Long itemId) {
        List<Booking> listOfBookings = bookingRepository.findAllByItemIdAndEndAfterOrderByEndDesc(itemId, Instant.now());
        if (listOfBookings.isEmpty()) {
            return null;
        }
        return listOfBookings.get(0);
    }
}
