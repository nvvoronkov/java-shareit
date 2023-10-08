package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.interfaces.Storage;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class ItemStorage implements Storage<Item> {
    private final Map<Long, Item> mapOfItems;
    private static long itemId = 0L;

    private Long generateItemId() {
        return ++itemId;
    }

    @Override
    public Item add(Item item) {
        Long itemId = generateItemId();
        item.setId(itemId);
        mapOfItems.put(itemId, item);
        return item;
    }

    @Override
    public Item update(Item itemForUpdate) {
        mapOfItems.put(itemForUpdate.getId(), itemForUpdate);
        return itemForUpdate;
    }

    @Override
    public Item getById(Long itemId) {
        return mapOfItems.get(itemId);
    }

    @Override
    public List<Item> getAll() {
        return new ArrayList<>(mapOfItems.values());
    }


    public List<Item> getAll(Long userId) {
        return mapOfItems.values().stream()
                .filter(item -> Objects.equals(item.getOwner(), userId))
                .collect(Collectors.toList());
    }

    @Override
    public String delete(Long userId) {
        mapOfItems.remove(userId);
        return String.format("Пользователь id=%s успешно удален.", userId);
    }

    @Override
    public Boolean checkIsObjectInStorage(Item item) {
        return mapOfItems.containsValue(item);
    }

    @Override
    public Boolean checkIsObjectInStorage(Long itemId) {
        return mapOfItems.containsKey(itemId);
    }

    public List<Item> search(String text) {

        return mapOfItems.values().stream()
                .filter(item -> item.getAvailable().equals(true))
                .filter(item -> StringUtils.containsIgnoreCase(item.getDescription(), text))
                .collect(Collectors.toList());
    }
}
