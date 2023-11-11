package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.WrongOwnerException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentReturnDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemReturnDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
@Validated
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestService itemRequestService;

    @Override
    public List<ItemReturnDto> getAllItemsByUser(long ownerId, int from, int size) {

        log.info("выполнется запрос на получение всех вещей пользователя | UserId - {}", ownerId);
        userService.getUserByIdIfExists(ownerId);

        List<Item> allUserItems = itemRepository.findAllByOwnerId(ownerId, PageRequest.of(from / size, size)).stream()
                .sorted(Comparator.comparing(Item::getId))
                .collect(Collectors.toList());

        Map<Long, List<Booking>> bookingMap = bookingRepository.findAllByItemInAndStatusOrderByStartAsc(allUserItems, BookingStatus.APPROVED)
                .stream()
                .collect(groupingBy(booking -> booking.getItem().getId(), toList()));

        Map<Long, List<Comment>> commentMap = commentRepository.findAllByItemIn(allUserItems).stream()
                .collect(groupingBy(commentDto -> commentDto.getItem().getId(), toList()));


        return allUserItems.stream()
                .map(it -> ItemMapper.toItemReturnDto(it,
                        findLastBooking(bookingMap.get(it.getId())),
                        findNextBooking(bookingMap.get(it.getId())),
                        mapperListComment(commentMap.get(it.getId()))))
                .collect(toList());
    }

    @Transactional
    @Override
    public ItemDto saveItem(long ownerId, ItemDto item) {

        log.info("выполняется запрос на добавление новой вещи");

        User owner = userService.getUserByIdIfExists(ownerId);
        ItemRequest itemRequest = null;
        if (item.getRequestId() != null) {
            itemRequest = itemRequestService.getItemRequestIfExists(item.getRequestId());
        }

        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(item, owner, itemRequest)));
    }

    @Transactional
    @Override
    public ItemDto updateItem(long ownerId, ItemDto item, long itemId) {

        log.info("выполняется запрос на обновление пользователя | UserId - {}", ownerId);
        Item forUpDate = getItemIfExists(itemId);
        itemOwnerValid(forUpDate, ownerId);

        if (item.getName() != null) forUpDate.setName(item.getName());
        if (item.getDescription() != null) forUpDate.setDescription(item.getDescription());
        if (item.getAvailable() != null) forUpDate.setAvailable(item.getAvailable());

        Item forRet = itemRepository.save(forUpDate);

        return ItemMapper.toItemDto(forRet);
    }

    @Override
    public ItemReturnDto getItemInfo(long id, long ownerId) {
        Item item = getItemIfExists(id);
        log.info("выполняется запрос на получение вещи по ID | UserId - {}", id);

        List<Booking> bookings = new ArrayList<>();
        if (item.getOwner().getId().equals(ownerId)) {
            bookings = bookingRepository.findAllByItemAndStatusOrderByStartAsc(item, BookingStatus.APPROVED);
        }

        List<Comment> comments = commentRepository.findAllByItem(item);

        return ItemMapper.toItemReturnDto(item,
                findLastBooking(bookings),
                findNextBooking(bookings),
                mapperListComment(comments));
    }

    @Override
    public List<ItemDto> searchItems(String text, int from, int size) {
        log.info("выполняется поиск по тексту text - {}", text);
        if (text.isBlank() || text.isEmpty()) return new ArrayList<>();

        return mapperListItem(itemRepository.findAllByNameAndDescription(text, PageRequest.of(from / size, size)));
    }

    @Override
    public Item getItemIfExists(long id) {
        log.info("выполняется запрос на получение пользователя по ID | UserId - {}", id);
        return itemRepository.findById(id).orElseThrow(() -> new DataNotFoundException("вещь с ID - " + id + " не найдена"));
    }

    @Transactional
    @Override
    public CommentReturnDto createComment(long userId, long itemId, CommentDto commentDto) {

        User author = userService.getUserByIdIfExists(userId);
        Item item = getItemIfExists(itemId);
        validCommentator(author, item);
        Comment forRet = commentRepository.save(CommentMapper.toComment(commentDto, item, author));
        return CommentMapper.toCommentReturnDto(forRet);
    }

    private void validCommentator(User commentator, Item item) {
        List<Booking> booking = bookingRepository.findAllByBookerAndItemAndStatus(commentator, item, LocalDateTime.now());
        if (booking == null || booking.isEmpty()) {
            throw new BookingException(String.format("пользователь с ID - %s не брал в аренду вещь с ID - %s",
                    commentator.getId(), item.getId()));
        }
    }

    private void itemOwnerValid(Item item, long ownerId) {
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new WrongOwnerException(String.format("пользователь с ID - %s не является владельцем вещи с ID - %s",
                    ownerId, item.getId()));
        }
    }

    private List<ItemDto> mapperListItem(List<Item> items) {
        return items.stream()
                .flatMap(i -> {
                    ItemDto dto = ItemMapper.toItemDto(i);
                    return Stream.of(dto);
                })
                .collect(toList());
    }

    private List<CommentReturnDto> mapperListComment(List<Comment> comments) {
        if (comments == null || comments.isEmpty()) {
            return new ArrayList<>();
        }
        return comments.stream()
                .map(CommentMapper::toCommentReturnDto)
                .collect(toList());
    }

    private BookingShortDto findLastBooking(List<Booking> bookings) {
        if (bookings == null || bookings.isEmpty()) {
            return null;
        }

        return bookings.stream()
                .filter(bookingDto -> !bookingDto.getStart().isAfter(LocalDateTime.now()))
                .reduce((b1, b2) -> b1.getStart().isAfter(b2.getStart()) ? b1 : b2)
                .map(BookingMapper::toBookingShortDto)
                .orElse(null);
    }

    private BookingShortDto findNextBooking(List<Booking> booking) {
        if (booking == null || booking.isEmpty()) {
            return null;
        }

        return booking.stream()
                .filter(bookingDto -> bookingDto.getStart().isAfter(LocalDateTime.now()))
                .findFirst()
                .map(BookingMapper::toBookingShortDto)
                .orElse(null);
    }
}
