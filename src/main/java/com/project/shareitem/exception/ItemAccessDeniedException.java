package com.project.shareitem.exception;

public class ItemAccessDeniedException extends RuntimeException {
    public ItemAccessDeniedException(Long itemId, Long userId) {
        super("Пользователь " + userId + " не является владельцем вещи " + itemId);
    }
}
