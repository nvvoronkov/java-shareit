package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repositiory.BookingRepository;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final UserServiceImpl userServiceImpl;

    @Override
    public ItemResponseResponseDto add(ItemRequestDto itemRequestDto) {
        //checkIsItemValid(itemRequestDto.getOwner());
        userServiceImpl.checkIsUserInStorage(itemRequestDto.getOwner());
        Item newItem = itemMapper.requestDtoToEntity(itemRequestDto);
        Item addedItem = itemRepository.save(newItem);
        ItemResponseResponseDto addedItemResponseDto = itemMapper.toDtoForOtherUsers(addedItem,
                Collections.emptyList());
        log.info(String.format("Объект id=%s успешно добавлен", addedItemResponseDto.getId()));
        return addedItemResponseDto;
    }

    @Override
    public ItemResponseResponseDto update(ItemRequestDto itemRequestDto) {
        userServiceImpl.checkIsUserInStorage(itemRequestDto.getOwner());
        checkIsItemInStorage(itemRequestDto.getId());
        Item itemFromStorage;
        Optional<Item> optionalItemFromStorage = itemRepository.findById(itemRequestDto.getId());
        if (optionalItemFromStorage.isEmpty()) {
            log.warn(String.format("Информация об объекте itemId=%s не найдена",
                    itemRequestDto.getId()));
            throw new ObjectNotFoundException(String.format("Информация об объекте itemId=%s не найдена",
                    itemRequestDto.getId()));
        } else {
            itemFromStorage = optionalItemFromStorage.get();
        }
        if (!Objects.equals(itemFromStorage.getOwner(), itemRequestDto.getOwner())) {
            log.warn(String.format("У пользователя userId=%s нет прав редактировать объект itemId=%s",
                    itemRequestDto.getOwner(), itemRequestDto.getId()));
            throw new ObjectNotFoundException(String.format("У пользователя userId=%s нет прав редактировать объект " +
                    "itemId=%s", itemRequestDto.getOwner(), itemRequestDto.getId()));
        }
        itemRequestDto.setId(Optional.ofNullable(itemRequestDto.getId()).orElse(itemFromStorage.getId()));
        itemRequestDto.setName(Optional.ofNullable(itemRequestDto.getName()).orElse(itemFromStorage.getName()));
        itemRequestDto.setDescription(Optional.ofNullable(itemRequestDto.getDescription())
                .orElse(itemFromStorage.getDescription()));
        itemRequestDto.setAvailable(Optional.ofNullable(itemRequestDto.getAvailable())
                .orElse(itemFromStorage.getAvailable()));
        Item updatedItem = itemRepository.save(itemMapper.requestDtoToEntity(itemRequestDto));
        List<CommentResponseDto> listOfComments = findListOfComments(itemRequestDto.getId());
        return itemMapper.toDtoForOtherUsers(updatedItem, listOfComments);
    }

    @Override
    public ResponseDto getById(Long userId, Long itemId) {
        userServiceImpl.checkIsUserInStorage(userId);
        checkIsItemInStorage(itemId);
        Item item = findById(itemId);
        List<CommentResponseDto> listOfComments = findListOfComments(itemId);
        if (userId.equals(item.getOwner())) {
            ItemResponseResponseDtoForOwner itemResponseDtoForOwner = itemMapper.toDtoForOwner(item, listOfComments);
            log.info(String.format("Объект itemId=%s успешно получен.", itemId));
            return itemResponseDtoForOwner;
        }
        ItemResponseResponseDto itemResponseDto = itemMapper.toDtoForOtherUsers(item, listOfComments);
        log.info(String.format("Объект itemId=%s успешно получен.", itemId));
        return itemResponseDto;
    }

    @Override
    public List<ItemResponseResponseDtoForOwner> getAllByOwner(Long userId) {
        userServiceImpl.checkIsUserInStorage(userId);
        List<Item> listOfItems = itemRepository.findAllByOwner(userId);
        if (listOfItems != null) {
            List<ItemResponseResponseDtoForOwner> listOfItemResponseDtoForOwners = listOfItems.stream()
                    .map(item -> itemMapper.toDtoForOwner(item, findListOfComments(item.getId()))).collect(Collectors.toList());
            log.info("Список объектов успешно получен.");
            return listOfItemResponseDtoForOwners;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public CommentResponseDto addComment(CommentRequestDto commentRequestDto) {
        userServiceImpl.checkIsUserInStorage(commentRequestDto.getAuthor());
        checkIsItemInStorage(commentRequestDto.getItem());
        if (commentRequestDto.getText().isBlank()) {
            log.warn("Текст комментария некорректный");
            throw new ValidationException("Текст комментария некорректный");
        }
        if (checkIsUserCanMakeComment(commentRequestDto.getAuthor(), commentRequestDto.getItem())) {
            Comment comment = commentMapper.toEntity(commentRequestDto);
            comment.setCreated(Instant.now());
            Comment savedComment = commentRepository.save(comment);
            return commentMapper.toCommentResponseDto(savedComment);
        } else {
            log.warn(String.format("Пользователь userId=%s не может оставить комментарий", commentRequestDto.getAuthor()));
            throw new ValidationException(String.format("Пользователь userId=%s не может оставить комментарий", commentRequestDto.getAuthor()));
        }
    }

    @Override
    public List<ItemResponseResponseDto> search(String text) {
        if (StringUtils.isBlank(text)) {
            return Collections.emptyList();
        }
        List<Item> listOfItems = itemRepository.search(text);
        List<ItemResponseResponseDto> listOfItemResponseDtoForOtherUsers = listOfItems.stream()
                .map(item -> itemMapper.toDtoForOtherUsers(item, findListOfComments(item.getId())))
                .collect(Collectors.toList());
        log.info("Список объектов успешно получен.");
        return listOfItemResponseDtoForOtherUsers;
    }

    @Override
    public String delete(Long userId) {
        itemRepository.deleteById(userId);
        log.info(String.format("Пользователь id=%s успешно удален.", userId));
        return String.format("Пользователь id=%s успешно удален.", userId);
    }

    @Override
    public void checkIsItemInStorage(Long itemId) {
        if (!itemRepository.existsById(itemId)) {
            log.warn(String.format("Объект itemId=%s не найден", itemId));
            throw new ObjectNotFoundException(String.format("Объект itemId=%s не найден", itemId));
        }
    }

    @Override
    public void checkIsItemAvailable(Long itemId) {
        if (!findById(itemId).getAvailable()) {
            log.warn(String.format("Объект itemId=%s не доступен", itemId));
            throw new ValidationException(String.format("Объект itemId=%s не доступен", itemId));
        }
    }

    @Override
    public void checkIsItemValid(Long itemId) {
        if (findById(itemId).getAvailable() == null) {
            log.warn(String.format("Объект itemId=%s не доступен", itemId));
            throw new ValidationException(String.format("Объект itemId=%s не доступен", itemId));
        }
    }

    private List<CommentResponseDto> findListOfComments(Long itemId) {
        return commentRepository.findByItem(itemId).stream()
                .map(commentMapper::toCommentResponseDto)
                .collect(Collectors.toList());
    }

    private Boolean checkIsUserCanMakeComment(Long userId, Long itemId) {
        List<Booking> result = bookingRepository.findAllByBookerIdAndItemIdAndEndIsBefore(userId, itemId, Instant.now());
        return !result.isEmpty();
    }

    public Item findById(Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) {
            log.warn(String.format("Информция об объекте itemId=%s не найдена", itemId));
            throw new ObjectNotFoundException(String.format("Информция об объекте itemId=%s не найдена", itemId));
        } else {
            return optionalItem.get();
        }
    }
}
