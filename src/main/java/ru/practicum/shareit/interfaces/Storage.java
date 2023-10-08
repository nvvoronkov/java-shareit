package ru.practicum.shareit.interfaces;

import java.util.List;

public interface Storage<T> {
    T add(T obj);

    T update(T obj);

    T getById(Long id);

    List<T> getAll();

    String delete(Long id);

    Boolean checkIsObjectInStorage(Long id);

    Boolean checkIsObjectInStorage(T obj);
}
