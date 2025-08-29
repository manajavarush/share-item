package com.project.shareitem.service;

import com.project.shareitem.dto.BookingCreateDto;
import com.project.shareitem.dto.BookingResponseDto;
import com.project.shareitem.entity.BookingStatus;
import com.project.shareitem.exception.BookingAccessDeniedException;
import com.project.shareitem.mapper.BookingMapper;
import com.project.shareitem.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final BookingUtils bookingUtils;
    private final ValidationService validationService;

    @Transactional
    public BookingResponseDto createBooking(BookingCreateDto bookingDto, Long userId) {
        var item = validationService.validateItemExists(bookingDto.itemId());
        var booker = validationService.validateUserExists(userId);
        validationService.validateBookingTime(bookingDto);
        validationService.validateItemAvailable(item);

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

    @Transactional(readOnly = true)
    public List<BookingResponseDto> getUserBookings(String state, Long userId) {
        validationService.validateUserExists(userId);

        var bookings = bookingRepository.findByBookerIdOrderByStartDesc(userId);

        var filteredBookings = bookingUtils.filterByState(bookings, state);

        log.info("Получение бронирований пользователя {} (как арендатора) со статусом {}", userId, state);

        return bookingMapper.mapToDtos(filteredBookings);
    }

    @Transactional(readOnly = true)
    public List<BookingResponseDto> getOwnerBookings(String state, Long ownerId) {
        validationService.validateUserExists(ownerId);
        validationService.validateItemExistsByOwnerId(ownerId);

        var bookings = bookingRepository.findByItemOwnerIdOrderByStartDesc(ownerId);

        var filteredBookings = bookingUtils.filterByState(bookings, state);

        log.info("Получение бронирований пользователя {} (как владельца вещей) со статусом {}", ownerId, state);

        return bookingMapper.mapToDtos(filteredBookings);
    }
}
