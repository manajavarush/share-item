package com.project.shareitem.service;


import com.project.shareitem.entity.Booking;
import com.project.shareitem.entity.BookingStatus;
import com.project.shareitem.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingUtils {
    private final BookingRepository bookingRepository;
    private final BookingStatus APPROVED = BookingStatus.APPROVED;

    public List<Booking> filterByState(List<Booking> bookings, String state) {
        LocalDateTime now = LocalDateTime.now();

        return bookings.stream()
                .filter(booking -> matchesState(booking, state, now))
                .toList();
    }

    private boolean matchesState(Booking booking, String state, LocalDateTime now) {
        return switch (state.toUpperCase()) {
            case "CURRENT" -> isCurrent(booking, now);
            case "PAST" -> isPast(booking, now);
            case "FUTURE" -> isFuture(booking, now);
            case "WAITING" -> booking.getStatus() == BookingStatus.WAITING;
            case "REJECTED" -> booking.getStatus() == BookingStatus.REJECTED;
            default -> true;
        };
    }

    public boolean isCurrent(Booking booking, LocalDateTime now) {
        return booking.getStart().isBefore(now) && booking.getEnd().isAfter(now);
    }

    public boolean isPast(Booking booking, LocalDateTime now) {
        return booking.getEnd().isBefore(now);
    }

    public boolean isFuture(Booking booking, LocalDateTime now) {
        return booking.getStart().isAfter(now);
    }

    public LocalDateTime findLastBookingDate(Long itemId, LocalDateTime now) {
        var lastBooking = bookingRepository.findFirstByItemIdAndStartBeforeAndStatusOrderByEndDesc(itemId, now, APPROVED);
        return lastBooking
                .map(Booking::getEnd)
                .orElse(null);
    }

    public LocalDateTime findNextBookingDate(Long itemId, LocalDateTime now) {
        var lastBooking = bookingRepository.findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(itemId, now, APPROVED);
        return lastBooking
                .map(Booking::getStart)
                .orElse(null);
    }
}
