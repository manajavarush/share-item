package com.project.shareitem.exception;

public class ItemNotAvailableException extends RuntimeException {
    public ItemNotAvailableException(Long id) {
        super("Вещь с id: " + id + " не доступна для бронирования");
    }
}
