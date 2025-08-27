package com.project.shareitem.exception;

public class UserNotOwnerException extends RuntimeException {
    public UserNotOwnerException(Long ownerId) {
        super("Пользователь "+ ownerId + " не владеет ни одной вещью");
    }
}
