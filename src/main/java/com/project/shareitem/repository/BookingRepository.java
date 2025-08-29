package com.project.shareitem.repository;

import com.project.shareitem.entity.Booking;
import com.project.shareitem.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId);

    Optional<Booking> findFirstByItemIdAndStartBeforeAndStatusOrderByEndDesc(Long itemId,
                                                                             LocalDateTime now,
                                                                             BookingStatus status);

    Optional<Booking> findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(Long itemId,
                                                                             LocalDateTime now,
                                                                             BookingStatus status);

    Boolean existsByBookerIdAndItemIdAndEndBefore(Long bookerId,
                                                  Long itemId,
                                                  LocalDateTime now);
}
