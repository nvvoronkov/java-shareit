package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.interfaces.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;

/*
если сделать класс статическим, тогда я не смогу имплементировать интерфейс Mapper
не очень понимаю зачем нужно тогда делать его статическим
 */
@Validated
@Component
public class ItemMapper implements Mapper<ItemDto, Item> {

    @Override
    public ItemDto toDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .request(item.getRequest())
                .build();
    }

    @Override
    @Valid
    public Item toEntity(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(itemDto.getOwner())
                .request(itemDto.getRequest())
                .build();
    }
}
