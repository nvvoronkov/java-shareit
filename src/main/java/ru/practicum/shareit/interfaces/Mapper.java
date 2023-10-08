package ru.practicum.shareit.interfaces;

public interface Mapper<T, E> {
    T toDto(E obj);

    E toEntity(T objDto);
}