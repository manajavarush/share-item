package com.project.shareitem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ItemAccessDeniedException extends RuntimeException {
    public ItemAccessDeniedException(Long itemId, Long userId) {
        super("Пользователь " + userId + " не является владельцем вещи " + itemId);
    }
}
