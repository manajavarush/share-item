package com.project.shareitem.exception;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException (String email) {
        super("Пользователь с данным email: " + email + " уже существует");
    }
}
