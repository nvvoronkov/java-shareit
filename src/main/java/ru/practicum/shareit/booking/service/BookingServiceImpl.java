package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingSaveDto;
import ru.practicum.shareit.booking.enums.BookingRequestState;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.BookingDataException;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Transactional
    @Override
    public BookingDto saveBooking(long userId, BookingSaveDto bookingSaveDto) {
        log.info("выполнется запрос на создание бронирования | UserId - {}", userId);
        Item item = getItemIfExistsAndAvailable(bookingSaveDto.getItemId());
        User user = userService.getUserByIdIfExists(userId);
        checkOwnerIsNotBooker(userId, item.getOwner().getId());
        validBookingStartEndDate(bookingSaveDto);

        Booking forSave = BookingMapper.toSaveBooking(bookingSaveDto, user, item);

        return BookingMapper.toBookingDto(bookingRepository.save(forSave));
    }

    @Transactional
    @Override
    public BookingDto approvingBooking(long possibleOwnerId, boolean approved, long bookingId) {
        log.info("выполнется запрос на подтверждение бронирования | UserId - {}", possibleOwnerId);
        userService.getUserByIdIfExists(possibleOwnerId);
        Booking forApproving = getBookingIfExists(bookingId);
        User owner = forApproving.getItem().getOwner();

        checkOwnerOfItem(owner, possibleOwnerId);


        if (approved) {
            if (forApproving.getStatus().equals(BookingStatus.APPROVED)) {
                throw new BookingException("Booking уже подтврежден");
            }
            forApproving.setStatus(BookingStatus.APPROVED);
        } else {
            forApproving.setStatus(BookingStatus.REJECTED);
        }

        return BookingMapper.toBookingDto(bookingRepository.save(forApproving));
    }

    @Override
    public BookingDto getBookingInfo(long bookingId, long userId) {
        log.info("выполнется запрос на получение бронирования | UserId - {}", userId);
        userService.getUserByIdIfExists(userId);
        Booking booking = getBookingIfExists(bookingId);
        checkOwnerOrBookerOfBooking(userId, booking.getBooker().getId(), booking.getItem().getOwner().getId());

        return BookingMapper.toBookingDto(booking);

    }

    @Override
    public List<BookingDto> getBookingInfoList(long bookerId, String state) {
        log.info("выполнется запрос на получение списка бронированиий по автору бронирования | UserId - {}", bookerId);
        User booker = userService.getUserByIdIfExists(bookerId);
        BookingRequestState bookingRequestState = BookingRequestState.valueOf(state);

        switch (bookingRequestState) {
            case ALL:
                return mapBookingListToDto(bookingRepository.findAllByBookerOrderByStartDesc(booker));

            case FUTURE:
                return mapBookingListToDto(bookingRepository.findAllByBookerForFuture(booker,
                        LocalDateTime.now()));

            case PAST:
                return mapBookingListToDto(bookingRepository.findAllPastBookingsByBooker(booker, LocalDateTime.now()));

            case CURRENT:
                return mapBookingListToDto(bookingRepository.findAllCurrentBookingsByBooker(booker, LocalDateTime.now()));

            case WAITING:
                return mapBookingListToDto(bookingRepository.findAllByBookerAndStatusOrderByStartDesc(booker, BookingStatus.WAITING));

            case REJECTED:
                return mapBookingListToDto(bookingRepository.findAllByBookerAndStatusOrderByStartDesc(booker, BookingStatus.REJECTED));

            default: {
                log.warn("Unknown state:" + bookingRequestState);
                throw new IllegalArgumentException("Unknown state:" + bookingRequestState);
            }
        }
    }

    @Override
    public List<BookingDto> getBookingOwnerInfoList(long ownerId, String state) {
        log.info("выполнется запрос на получение списка бронирований по владельцу вещи | UserId - {}", ownerId);
        User owner = userService.getUserByIdIfExists(ownerId);
        BookingRequestState bookingRequestState = BookingRequestState.valueOf(state);
        switch (bookingRequestState) {

            case ALL:
                return mapBookingListToDto(bookingRepository.findAllByOwnerOrderByStartDesc(owner));

            case FUTURE:
                return mapBookingListToDto(bookingRepository.findAllByOwnerForFuture(owner,
                        LocalDateTime.now()));

            case PAST:
                return mapBookingListToDto(bookingRepository.findAllPastBookingsByOwner(owner, LocalDateTime.now()));

            case CURRENT:
                return mapBookingListToDto(bookingRepository.findAllCurrentBookingsByOwner(owner, LocalDateTime.now()));

            case WAITING:
                return mapBookingListToDto(bookingRepository.findAllByOwnerAndStatusOrderByStartDesc(owner, BookingStatus.WAITING));

            case REJECTED:
                return mapBookingListToDto(bookingRepository.findAllByOwnerAndStatusOrderByStartDesc(owner, BookingStatus.REJECTED));

            default: {
                log.warn("Unknown state:" + bookingRequestState);
                throw new IllegalArgumentException("Unknown state:" + bookingRequestState);
            }
        }
    }

    private Booking getBookingIfExists(long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingDataException("Бронирование с ID - " + bookingId + " не найдено"));
    }

    private Item getItemIfExistsAndAvailable(long itemId) {
        Item item = itemService.getItemIfExists(itemId);
        if (!item.getAvailable()) throw new BookingException("Вещь c ID - " + itemId + " недоступна для бронирования");
        return item;
    }

    private void validBookingStartEndDate(BookingSaveDto bookingSaveDto) {
        if (bookingSaveDto.getStart().isBefore(LocalDateTime.now()))
            throw new BookingException("Начало бронирования не " +
                                       "может быть раньше текущей даты");
        if (bookingSaveDto.getEnd().isBefore(LocalDateTime.now())) throw new BookingException("Конец бронирования не " +
                                                                                              "может быть раньше текущей даты");
        if (bookingSaveDto.getEnd().isBefore(bookingSaveDto.getStart()))
            throw new BookingException("Начало бронирования " +
                                       "не может быть позже окончания");
        if (bookingSaveDto.getStart().isEqual(bookingSaveDto.getEnd()))
            throw new BookingException("Начало и окончание " +
                                       "бронирования должны быть разными датами");
    }

    private void checkOwnerOfItem(User owner, long possibleOwnerId) {
        if (owner.getId() != possibleOwnerId)
            throw new BookingDataException("Только владельцы вещей могут подтвердить бронирование");
    }

    private void checkOwnerOrBookerOfBooking(long userId, long bookerId, long ownerId) {
        if (!((userId == bookerId) || (userId == ownerId)))
            throw new BookingDataException("Только владельцы вещи или автор бронирования может смотреть бронирование");
    }

    private List<BookingDto> mapBookingListToDto(List<Booking> bookingList) {
        return bookingList.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    private void checkOwnerIsNotBooker(long userId, long itemId) {
        if (userId == itemId) throw new BookingDataException("Владелец вещи не может арендовать вещь");
    }
}
