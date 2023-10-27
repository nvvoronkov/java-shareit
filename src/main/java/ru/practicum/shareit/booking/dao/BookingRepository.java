package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT DISTINCT b " +
            "FROM Booking b " +
            "JOIN FETCH b.booker " +
            "WHERE b.booker = :booker " +
            "AND b.start > :currentTime " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByBookerForFuture(User booker, LocalDateTime currentTime);

    List<Booking> findAllByBookerOrderByStartDesc(User booker);

    List<Booking> findAllByBookerAndStatusOrderByStartDesc(User booker, BookingStatus status);

    @Query("SELECT DISTINCT b " +
            "FROM Booking b " +
            "JOIN FETCH b.item as bi " +
            "WHERE bi.owner = :owner " +
            "AND b.start > :currentTime " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwnerForFuture(User owner, LocalDateTime currentTime);

    @Query("SELECT DISTINCT b " +
            "FROM Booking b " +
            "JOIN FETCH b.item as bi " +
            "WHERE bi.owner = :owner " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwnerOrderByStartDesc(User owner);

    @Query("SELECT DISTINCT b " +
            "FROM Booking b " +
            "JOIN FETCH b.item as bi " +
            "WHERE bi.owner = :owner " +
            "AND b.status = :status " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwnerAndStatusOrderByStartDesc(User owner, BookingStatus status);

    List<Booking> findAllByItemInAndStatusOrderByStartAsc(List<Item> items, BookingStatus status);

    List<Booking> findAllByItemAndStatusOrderByStartAsc(Item items, BookingStatus status);


    @Query("SELECT DISTINCT b " +
            "FROM Booking b " +
            "     JOIN FETCH b.item as bi " +
            "     JOIN FETCH b.booker as bb " +
            "WHERE bb = :booker " +
            "AND bi = :item " +
            "AND b.status = 'APPROVED'" +
            "AND b.end < :currentTime ")
    List<Booking> findAllByBookerAndItemAndStatus(User booker, Item item, LocalDateTime currentTime);

    @Query("SELECT DISTINCT b " +
            "FROM Booking b " +
            "JOIN FETCH b.booker as bb " +
            "WHERE bb = :booker " +
            "AND :currentTime BETWEEN b.start AND b.end " +
            "ORDER BY b.start DESC ")
    List<Booking> findAllCurrentBookingsByBooker(User booker, LocalDateTime currentTime);

    @Query("SELECT DISTINCT b " +
            "FROM Booking b " +
            "JOIN FETCH b.item bi " +
            "WHERE bi.owner = :owner " +
            "AND :currentTime BETWEEN b.start AND b.end " +
            "ORDER BY b.start DESC ")
    List<Booking> findAllCurrentBookingsByOwner(User owner, LocalDateTime currentTime);

    @Query("SELECT DISTINCT b " +
            "FROM Booking b " +
            "JOIN FETCH b.booker bb " +
            "WHERE bb = :booker " +
            "AND :currentTime > b.end " +
            "ORDER BY b.start DESC ")
    List<Booking> findAllPastBookingsByBooker(User booker, LocalDateTime currentTime);

    @Query("SELECT DISTINCT b " +
            "FROM Booking b " +
            "JOIN FETCH b.item bi " +
            "WHERE bi.owner = :owner " +
            "AND :currentTime > b.end " +
            "ORDER BY b.start DESC ")
    List<Booking> findAllPastBookingsByOwner(User owner, LocalDateTime currentTime);
}
