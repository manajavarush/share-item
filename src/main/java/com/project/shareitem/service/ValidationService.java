package com.project.shareitem.service;

import com.project.shareitem.entity.Booking;
import com.project.shareitem.entity.Item;
import com.project.shareitem.entity.User;
import com.project.shareitem.exception.*;
import com.project.shareitem.repository.BookingRepository;
import com.project.shareitem.repository.ItemRepository;
import com.project.shareitem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ValidationService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    public User validateUserExists(Long userId) {
        log.debug("Проверка существования пользователя с userId: {}", userId);

        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Пользователь с id: {} не найден", userId);
                    return new UserNotFoundException(userId);
                });
    }

    public Item validateItemExists(Long itemId) {
        log.debug("Проверка существования вещи с id: {}", itemId);

        return itemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.warn("Вещь с id: {} не найдена", itemId);
                    return new ItemNotFoundException(itemId);
                });
    }

    public Booking validateBookingExists(Long bookingId) {
        log.debug("Проверка существования бронирования с bookingId: {}", bookingId);

        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    log.warn("Бронирование с id: {} не найдено", bookingId);
                    return new BookingNotFoundException(bookingId);
                });
    }


    public void validateItemOwner(Long itemId, long userId) {
        log.debug("Проверка владения вещью {} пользователем {}", itemId, userId);
        var item = validateItemExists(itemId);

        if (!item.getOwner().getId().equals(userId)) {
            log.warn("Пользователь с id: {} не является владельцем вещи с id: {}", userId, itemId);
            throw new ItemAccessDeniedException(itemId, userId);
        }
    }

    public void validateEmailNotExists(String email) {
        log.debug("Проверка уникальности email: {}", email);

        if (userRepository.existsByEmail(email)) {
            log.warn("Email {} уже используется", email);
            throw new EmailAlreadyExistsException(email);
        }
    }

    public void validateItemExistsByOwnerId(Long ownerId) {
        log.debug("Проверка владения хотя бы одной вещью пользователем {}", ownerId);

        if (!itemRepository.existsByOwnerId(ownerId)) {
            log.warn("Пользователь с id: {} не владеет ни одной вещью", ownerId);
            throw new UserNotOwnerException(ownerId);
        }
    }
}
