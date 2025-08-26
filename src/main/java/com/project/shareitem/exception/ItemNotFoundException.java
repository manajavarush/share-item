package com.project.shareitem.exception;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(Long id) {
        super("Вещь с id: " + id + " не найдена");
    }
}
