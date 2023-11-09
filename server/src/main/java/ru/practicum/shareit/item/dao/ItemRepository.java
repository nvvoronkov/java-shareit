package ru.practicum.shareit.item.dao;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerId(long ownerId, PageRequest pageRequest);

    @Query("SELECT i FROM Item i WHERE ((lower(i.name) like concat('%' , lower(?1), '%'))  OR lower(i.description) like concat('%' , lower(?1), '%')) AND i.available = true")
    List<Item> findAllByNameAndDescription(String text, PageRequest pageRequest);

    List<Item> findAllByRequestInOrderByIdAsc(List<ItemRequest> requests);

    List<Item> findAllByRequest(ItemRequest request);
}
