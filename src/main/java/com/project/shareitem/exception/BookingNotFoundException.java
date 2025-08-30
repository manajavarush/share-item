package com.project.shareitem.exception;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(Long id) {
        super("Бронь с id: " + id + " не найдена");
    }
}
