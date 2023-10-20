package ru.practicum.shareit.booking.repositiory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.enums.BookingStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(Long userId);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long bookerId, Instant currentTime1, Instant currentTime2);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long userId, Instant currentTime);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long userId, Instant currentTime);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long userId, BookingStatus state);

    @Query(" select b " +
            "from Booking b left join Item i on b.itemId = i.id " +
            "where i.owner = ?1 " +
            "order by b.start desc ")
    List<Booking> findAllBookingsByItemOwner(Long userId);

    @Query(" select b " +
            "from Booking b left join Item i on b.itemId = i.id " +
            "where i.owner = ?1 and " +
            "b.start < ?2  and " +
            "b.end > ?2 " +
            "order by b.start desc ")
    List<Booking> findAllCurrentBookingsByItemOwner(Long userId, Instant currentTime);

    @Query(" select b " +
            "from Booking b left join Item i on b.itemId = i.id " +
            "where i.owner = ?1 and " +
            "b.start > ?2  and " +
            "b.end > ?2 " +
            "order by b.start desc ")
    List<Booking> findAllFutureBookingsByItemOwner(Long userId, Instant currentTime);

    @Query(" select b " +
            "from Booking b left join Item i on b.itemId = i.id " +
            "where i.owner = ?1 and " +
            "b.start < ?2  and " +
            "b.end < ?2 " +
            "order by b.start desc ")
    List<Booking> findAllPastBookingsByItemOwner(Long userId, Instant currentTime);

    @Query(" select b " +
            "from Booking b left join Item i on b.itemId = i.id " +
            "where i.owner = ?1 and " +
            "b.status = ?2 " +
            "order by b.start desc ")
    List<Booking> findAllWaitingBookingsByItemOwner(Long userId, BookingStatus state);

    @Query(" select b " +
            "from Booking b left join Item i on b.itemId = i.id " +
            "where i.owner = ?1 and " +
            "b.status = ?2 " +
            "order by b.start desc ")
    List<Booking> findAllRejectedBookingsByItemOwner(Long userId, BookingStatus state);

    List<Booking> findAllByItemIdAndEndBeforeOrderByEndDesc(Long itemId, Instant currentTime);

    List<Booking> findAllByItemIdAndEndAfterOrderByEndDesc(Long itemId, Instant currentTime);

    List<Booking> findAllByBookerIdAndItemIdAndEndIsBefore(Long bookerId, Long itemId, Instant currentTime);

    Optional<Booking> findByItemIdAndEndAfterAndStartBeforeOrderByStartDesc(Long itemId, Instant start, Instant end);
}
