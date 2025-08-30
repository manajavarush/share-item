package com.project.shareitem.exception;

public class BookingAccessDeniedException extends RuntimeException {
    public BookingAccessDeniedException(Long bookingId, Long userId) {
        super("Пользователь " + userId + " не имеет доступа к брони " + bookingId);
    }
}
