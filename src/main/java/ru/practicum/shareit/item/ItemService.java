package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.interfaces.Mapper;
import ru.practicum.shareit.interfaces.Services;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ItemService implements Services<ItemDto> {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final Mapper<ItemDto, Item> itemMapper;

    @Override
    public ItemDto add(ItemDto itemDto) {
        if (!userStorage.checkIsObjectInStorage(itemDto.getOwner())) {
            log.warn(String.format("Пользователь userId=%s не найден", itemDto.getOwner()));
            throw new ObjectNotFoundException(String.format("Пользователь userId=%s не найден", itemDto.getOwner()));
        }
        Item addedItem = itemStorage.add(itemMapper.toEntity(itemDto));
        ItemDto addedItemDto = itemMapper.toDto(addedItem);
        log.info(String.format("Объект id=%s успешно добавлен", addedItemDto.getId()));
        return addedItemDto;
    }

    @Override
    public ItemDto update(ItemDto itemDtoForUpdate) {
        if (!userStorage.checkIsObjectInStorage(itemDtoForUpdate.getOwner())) {
            log.warn(String.format("Пользователь userId=%s не найден", itemDtoForUpdate.getOwner()));
            throw new ObjectNotFoundException(String.format("Пользователь userId=%s не найден", itemDtoForUpdate.getOwner()));
        }
        if (!itemStorage.checkIsObjectInStorage(itemDtoForUpdate.getId())) {
            log.warn(String.format("Объект itemId=%s не найден", itemDtoForUpdate.getId()));
            throw new ObjectNotFoundException(String.format("Объект itemId=%s не найден", itemDtoForUpdate.getId()));
        }
        if (!Objects.equals(itemStorage.getById(itemDtoForUpdate.getId()).getOwner(), itemDtoForUpdate.getOwner())) {
            log.warn(String.format("У пользователя userId=%s нет объекта itemId=%s",
                    itemDtoForUpdate.getOwner(), itemDtoForUpdate.getId()));
            throw new ObjectNotFoundException(String.format("У пользователя userId=%s нет объекта itemId=%s",
                    itemDtoForUpdate.getOwner(), itemDtoForUpdate.getId()));
        }
        Item itemFromStorage = itemStorage.getById(itemDtoForUpdate.getId());
        itemDtoForUpdate.setId(Optional.ofNullable(itemDtoForUpdate.getId()).orElse(itemFromStorage.getId()));
        itemDtoForUpdate.setName(Optional.ofNullable(itemDtoForUpdate.getName()).orElse(itemFromStorage.getName()));
        itemDtoForUpdate.setDescription(Optional.ofNullable(itemDtoForUpdate.getDescription())
                .orElse(itemFromStorage.getDescription()));
        itemDtoForUpdate.setAvailable(Optional.ofNullable(itemDtoForUpdate.getAvailable())
                .orElse(itemFromStorage.getAvailable()));
        itemStorage.update(itemMapper.toEntity(itemDtoForUpdate));
        return itemDtoForUpdate;
    }

    @Override
    public ItemDto getById(Long itemId) {
        if (!itemStorage.checkIsObjectInStorage(itemId)) {
            log.warn(String.format("Объект itemId=%s не найден", itemId));
            throw new ObjectNotFoundException(String.format("Объект itemId=%s не найден", itemId));
        }
        Item item = itemStorage.getById(itemId);
        ItemDto itemDto = itemMapper.toDto(item);
        log.info(String.format("Объект itemId=%s успешно получен.", itemId));
        return itemDto;
    }

    @Override
    public List<ItemDto> getAll() {
        List<Item> listOfItems = itemStorage.getAll();
        List<ItemDto> listOfItemDto = listOfItems.stream().map(itemMapper::toDto).collect(Collectors.toList());
        log.info("Список объектов успешно получен.");
        return listOfItemDto;
    }

    public List<ItemDto> getAll(Long userId) {
        List<Item> listOfItems = itemStorage.getAll(userId);
        List<ItemDto> listOfItemDto = listOfItems.stream().map(itemMapper::toDto).collect(Collectors.toList());
        log.info("Список объектов успешно получен.");
        return listOfItemDto;
    }

    public List<ItemDto> search(String text) {
        if (StringUtils.isBlank(text)) {
            return Collections.emptyList();
        }
        List<Item> listOfItems = itemStorage.search(text);
        List<ItemDto> listOfItemDto = listOfItems.stream().map(itemMapper::toDto).collect(Collectors.toList());
        log.info("Список объектов успешно получен.");
        return listOfItemDto;
    }

    @Override
    public String delete(Long userId) {
        String message = itemStorage.delete(userId);
        log.info(message);
        return message;
    }


}
