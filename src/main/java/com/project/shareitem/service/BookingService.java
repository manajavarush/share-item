package com.project.shareitem.service;

import com.project.shareitem.dto.BookingCreateDto;
import com.project.shareitem.dto.BookingResponseDto;
import com.project.shareitem.entity.Booking;
import com.project.shareitem.entity.BookingStatus;
import com.project.shareitem.exception.BookingAccessDeniedException;
import com.project.shareitem.exception.InvalidBookingTimeException;
import com.project.shareitem.exception.ItemNotAvailableException;
import com.project.shareitem.mapper.BookingMapper;
import com.project.shareitem.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final ValidationService validationService;

    @Transactional
    public BookingResponseDto createBooking(BookingCreateDto bookingDto, Long userId) {
        var item = validationService.validateItemExists(bookingDto.itemId());
        var booker = validationService.validateUserExists(userId);
        validateBookingTime(bookingDto);

        if (!item.getAvailable()) {
            log.warn("Попытка забронировать недоступную вещь с id:{}", item.getId());
            throw new ItemNotAvailableException(item.getId());
        }

        var booking = bookingMapper.toEntity(bookingDto);

        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        var savedBooking = bookingRepository.save(booking);
        log.info("Пользователь с id:{} забронировал вещь с id:{}, бронь:{}",
                userId, item.getId(), savedBooking.getId());

        return bookingMapper.toResponseDto(savedBooking);
    }

    @Transactional
    public BookingResponseDto approveBooking(Long bookingId, Boolean approved, Long userId) {
        var booking = validationService.validateBookingExists(bookingId);
        validationService.validateItemOwner(booking.getItem().getId(), userId);

        booking.setStatus(Boolean.TRUE.equals(approved) ?
                BookingStatus.APPROVED :
                BookingStatus.REJECTED);

        var updatedBooking = bookingRepository.save(booking);
        log.info("Пользователь с id:{} {}  бронь:{}",
                userId,
                approved ? "подтвердил" : "отклонил",
                updatedBooking.getId());

        return bookingMapper.toResponseDto(updatedBooking);
    }

    @Transactional(readOnly = true)
    public BookingResponseDto getBookingById(Long bookingId, Long userId) {
        var booking = validationService.validateBookingExists(bookingId);
        var isBooker = Objects.equals(booking.getBooker().getId(), userId);
        var isOwner = Objects.equals(booking.getItem().getOwner().getId(), userId);

        if (!isOwner && !isBooker) {
            throw new BookingAccessDeniedException(bookingId, userId);
        }

        log.info("Получена бронь с bookingId:{}", bookingId);

        return bookingMapper.toResponseDto(booking);
    }

    public List<BookingResponseDto> getUserBookings(String state, Long userId) {
        validationService.validateUserExists(userId);

        var bookings = bookingRepository.findByBookerIdOrderByStartDesc(userId);

        var filteredBookings = filterByState(bookings, state);
        log.info("Получение бронирований пользователя {} (как арендатора) со статусом {}", userId, state);

        return mapToDtos(filteredBookings);
    }

    public List<BookingResponseDto> getOwnerBookings(String state, Long ownerId) {
        validationService.validateUserExists(ownerId);
        validationService.validateItemExistsByOwnerId(ownerId);

        var bookings = bookingRepository.findByItemOwnerIdOrderByStartDesc(ownerId);

        var filteredBookings = filterByState(bookings, state);
        log.info("Получение бронирований пользователя {} (как владельца вещей) со статусом {}", ownerId, state);

        return mapToDtos(filteredBookings);
    }

    private List<Booking> filterByState(List<Booking> bookings, String state) {
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
            default -> true; // ALL by default
        };
    }

    private boolean isCurrent(Booking booking, LocalDateTime now) {
        return booking.getStart().isBefore(now) && booking.getEnd().isAfter(now);
    }

    private boolean isPast(Booking booking, LocalDateTime now) {
        return booking.getEnd().isBefore(now);
    }

    private boolean isFuture(Booking booking, LocalDateTime now) {
        return booking.getStart().isAfter(now);
    }

    private List<BookingResponseDto> mapToDtos(List<Booking> bookings) {
        if (bookings == null || bookings.isEmpty()) {
            return Collections.emptyList();
        }

        return bookings.stream()
                .filter(Objects::nonNull)
                .map(bookingMapper::toResponseDto)
                .toList();
    }

    private void validateBookingTime(BookingCreateDto bookingDto) {
        var start = bookingDto.start();
        var end = bookingDto.end();
        var now = LocalDateTime.now();

        log.debug("Проверка дат бронирования: начало={}, окончание={}, текущая дата и время={}",
                start, end, now);

        if (start == null) {
            //    log.warn("Start equals null");  ??? избыточно ???
            throw new InvalidBookingTimeException("Booking create failed by start equals null");
        }

        if (end == null) {
            throw new InvalidBookingTimeException("Booking create failed by end equals null");
        }

        if (end.isBefore(now)) {
            throw new InvalidBookingTimeException("Booking create failed by end in past");
        }

        if (start.isBefore(now)) {
            throw new InvalidBookingTimeException("Booking create failed by start in past");
        }

        if (start.equals(end)) {
            throw new InvalidBookingTimeException("Booking create failed by start equals end");
        }
    }
}
