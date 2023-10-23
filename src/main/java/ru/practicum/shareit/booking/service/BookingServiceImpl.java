package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.RequestState;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.repositiory.BookingRepository;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.UnsupportedStatusException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserServiceImpl userServiceImpl;
    private final ItemServiceImpl itemServiceImpl;

    @Override
    public BookingResponseDto add(BookingRequestDto bookingRequestDto) {
        userServiceImpl.checkIsUserInStorage(bookingRequestDto.getBookerId());
        itemServiceImpl.checkIsItemInStorage(bookingRequestDto.getItemId());
        itemServiceImpl.checkIsItemAvailable(bookingRequestDto.getItemId());
        Booking booking = bookingMapper.requestDtoToEntity(bookingRequestDto);
        checkBookingTime(booking);
        checkIsItemCanBeBooked(booking.getItemId(), booking.getStart(), booking.getEnd());
        if (checkIsUserOwnerOfItem(booking.getBookerId(), booking.getItemId())) {
            log.warn(String.format("Данные о бронировании не доступны  для пользователя id=%s.",
                    bookingRequestDto.getBookerId()));
            throw new ObjectNotFoundException(String.format("Данные о бронировании не доступны  " +
                    "для пользователя id=%s.", bookingRequestDto.getBookerId()));
        }
        booking.setStatus(BookingStatus.WAITING);
        Booking savedBooking = bookingRepository.save(booking);
        log.info(String.format("Бронирование id=%s успешно добавлено.", savedBooking.getId()));
        return bookingMapper.entityToResponseDto(savedBooking);
    }

    @Override
    public BookingResponseDto update(Long userId, Long bookingId, Boolean isApproved) {
        userServiceImpl.checkIsUserInStorage(userId);
        checkIsBookingInStorage(bookingId);
        checkIsUserOwnerOfBookedItem(userId, bookingId);
        Booking bookingForUpdate = findById(bookingId);
        if (bookingForUpdate.getStatus() == (BookingStatus.APPROVED)) {
            log.warn(String.format("Статус бронирование id=%s не может  быть изменен.", bookingId));
            throw new ValidationException(String.format("Статус бронирование id=%s не может  быть изменен.",
                    bookingId));
        }
        if (userId.equals(itemServiceImpl.findById(bookingForUpdate.getItemId()).getOwner())) {
            bookingForUpdate.setStatus(isApproved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        }
        if (userId.equals(bookingForUpdate.getBookerId())) {
            bookingForUpdate.setStatus(isApproved ? BookingStatus.WAITING : BookingStatus.CANCELED);
        }
        Booking updatedBooking = bookingRepository.save(bookingForUpdate);
        BookingResponseDto bookingResponseDto = bookingMapper.entityToResponseDto(updatedBooking);
        log.info(String.format("Бронирование id=%s успешно обновлено.", bookingId));
        return bookingResponseDto;
    }

    @Override
    public BookingResponseDto getById(Long userId, Long bookingId) {
        userServiceImpl.checkIsUserInStorage(userId);
        checkIsBookingInStorage(bookingId);
        if (!(checkIsUserCreatorOfBooking(userId, bookingId) || checkIsUserOwnerOfBookedItem(userId, bookingId))) {
            log.warn(String.format("Данные о бронировании не доступны  для пользователя id=%s.", userId));
            throw new ObjectNotFoundException(String.format("Данные о бронировании не доступны  для " +
                    "пользователя id=%s.", userId));
        }
        Booking booking = findById(bookingId);
        BookingResponseDto bookingResponseDto = bookingMapper.entityToResponseDto(booking);
        log.info(String.format("Бронирование id=%s успешно получено.", bookingId));
        return bookingResponseDto;
    }

    @Override
    public List<BookingResponseDto> getAllBookingsByBookerId(Long userId, String state) {
        userServiceImpl.checkIsUserInStorage(userId);
        List<Booking> result;
        switch (RequestState.valueOf(state)) {
            case ALL: {
                result = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
                break;
            }
            case CURRENT: {
                result = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                        Instant.now(), Instant.now());
                break;
            }
            case FUTURE: {
                result = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, Instant.now());
                break;
            }
            case PAST: {
                result = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, Instant.now());
                break;
            }
            case WAITING: {
                result = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId,
                        BookingStatus.WAITING);
                break;
            }
            case REJECTED: {
                result = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
                break;
            }
            default: {
                log.warn("Unknown state: UNSUPPORTED_STATUS");
                throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
            }
        }
        return result.stream().map(bookingMapper::entityToResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getAllBookingsByOwnerItems(Long userId, String state) {
        userServiceImpl.checkIsUserInStorage(userId);
        List<Booking> result;
        switch (RequestState.valueOf(state)) {
            case ALL: {
                result = bookingRepository.findAllBookingsByItemOwner(userId);
                break;
            }
            case CURRENT: {
                result = bookingRepository.findAllCurrentBookingsByItemOwner(userId, Instant.now());
                break;
            }
            case FUTURE: {
                result = bookingRepository.findAllFutureBookingsByItemOwner(userId, Instant.now());
                break;
            }
            case PAST: {
                result = bookingRepository.findAllPastBookingsByItemOwner(userId, Instant.now());
                break;
            }
            case WAITING: {
                result = bookingRepository.findAllWaitingBookingsByItemOwner(userId, BookingStatus.WAITING);
                break;
            }
            case REJECTED: {
                result = bookingRepository.findAllRejectedBookingsByItemOwner(userId, BookingStatus.REJECTED);
                break;
            }
            default: {
                log.warn("Unknown state: UNSUPPORTED_STATUS");
                throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
            }
        }
        return result.stream().map(bookingMapper::entityToResponseDto).collect(Collectors.toList());
    }

    private Booking findById(Long bookingId) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isPresent()) {
            return optionalBooking.get();
        } else {
            log.warn(String.format("Ифнормация о бронировании id=%s не найдена.", bookingId));
            throw new ObjectNotFoundException(String.format("Ифнормация о бронировании id=%s не найдена.", bookingId));
        }
    }

    private void checkIsItemCanBeBooked(Long itemId, Instant start, Instant end) {
        Optional<Booking> optionalBooking = bookingRepository
                .findByItemIdAndEndAfterAndStartBeforeOrderByStartDesc(itemId, start, end);
        if (!optionalBooking.isEmpty()) {
            log.warn(String.format("Объект id=%s не доступен для бронирования.", itemId));
            throw new ValidationException(String.format("Объект id=%s не доступен для бронирования.", itemId));
        }
    }

    private void checkBookingTime(Booking booking) {
        if (booking.getStart().isBefore(Instant.now())) {
            log.warn(String.format("Некорректное время начала бронирования объекта id=%s.", booking.getBookerId()));
            throw new ValidationException(String.format("Некорректное время начала бронирования объекта id=%s.",
                    booking.getBookerId()));
        }
        if (booking.getEnd().isBefore(booking.getStart())) {
            log.warn(String.format("Некорректное время окончания бронирования объекта id=%s.", booking.getBookerId()));
            throw new ValidationException(String.format("Некорректное время окончания бронирования объекта id=%s.",
                    booking.getBookerId()));
        }
    }

    private void checkIsBookingInStorage(Long bookingId) {
        if (!bookingRepository.existsById(bookingId)) {
            log.warn(String.format("Бронирование id=%s не найдено.", bookingId));
            throw new ObjectNotFoundException(String.format("Бронирование id=%s не найдено.", bookingId));
        }
    }

    private boolean checkIsUserCreatorOfBooking(Long userId, Long bookingId) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isPresent()) {
            return userId.equals(optionalBooking.get().getBookerId());
        } else {
            log.warn(String.format("Информация о бронировании id=%s не найдена.", bookingId));
            throw new ObjectNotFoundException(String.format("Информация о бронировании id=%s не найдена.", bookingId));
        }
    }

    private boolean checkIsUserOwnerOfItem(Long userId, Long itemId) {
        Item item = itemServiceImpl.findById(itemId);
        return userId.equals(item.getOwner());
    }

    private boolean checkIsUserOwnerOfBookedItem(Long userId, Long bookingId) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        Booking booking;
        if (optionalBooking.isPresent()) {
            booking = optionalBooking.get();
        } else {
            log.warn(String.format("Бронирование id=%s не найдено.", bookingId));
            throw new ObjectNotFoundException(String.format("Бронирование id=%s не найдено.", bookingId));
        }
        Item item = itemServiceImpl.findById(booking.getItemId());
        if (userId.equals(item.getOwner())) {
            return true;
        } else {
            log.warn(String.format("Данные о бронировании не доступны  для пользователя id=%s.", userId));
            throw new ObjectNotFoundException(String.format("Данные о бронировании не доступны  для пользователя " +
                    "id=%s.", userId));
        }
    }
}