package ru.practicum.shareit.interfaces;

import java.util.List;

public interface Service<T> {
    T add(T obj);

    T update(T objForUpdate);

    T getById(Long id);

    List<T> getAll();

    String delete(Long id);
}
