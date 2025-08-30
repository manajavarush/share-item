package com.project.shareitem.repository;

import com.project.shareitem.entity.Booking;
import com.project.shareitem.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    String FIND_BY_BOOKER_ID = """
            SELECT b
            FROM Booking b
            WHERE b.booker.id = :bookerId
            ORDER BY b.start DESC
            """;

    String FIND_BY_OWNER_ID = """
            SELECT b
            FROM Booking b
            WHERE b.item.id = :itemId
            ORDER BY b.start DESC
            """;

    String FIND_LAST_BOOKING = """
                SELECT b
                FROM Booking b
                WHERE b.item.id = :itemId
                  AND b.start < :now
                  AND b.status = :status
                ORDER BY b.end DESC
                LIMIT 1
            """;

    String FIND_NEXT_BOOKING = """
            SELECT b
            FROM Booking b
            WHERE b.item.id = :itemId
            AND b.start > :now
            AND b.status = :status
            ORDER BY b.start ASC
            LIMIT 1
            """;

    @Query(FIND_BY_BOOKER_ID)
    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    @Query(FIND_BY_OWNER_ID)
    List<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId);

    @Query(FIND_LAST_BOOKING)
    Optional<Booking> findLastBookingForItem(Long itemId, LocalDateTime now, BookingStatus status);

    @Query(FIND_NEXT_BOOKING)
    Optional<Booking> findNextBookingForItem(Long itemId, LocalDateTime now, BookingStatus status);

    Boolean existsByBookerIdAndItemIdAndEndBefore(Long bookerId, Long itemId, LocalDateTime now);
}
