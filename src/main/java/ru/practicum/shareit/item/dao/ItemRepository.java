package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwnerId(long ownerId);

    @Query("SELECT i FROM Item i WHERE ((lower(i.name) like concat('%' , lower(?1), '%'))  OR lower(i.description) like concat('%' , lower(?1), '%')) AND i.available = true")
    List<Item> findAllByNameAndDescription(String text);
}
