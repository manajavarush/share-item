package com.project.shareitem.handler;

import com.project.shareitem.dto.ApiError;
import com.project.shareitem.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ApiError handleEmailExists(EmailAlreadyExistsException exception) {
        return buildErrorResponse(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ItemAccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ApiError handleItemAccessDenied(ItemAccessDeniedException exception) {
        return buildErrorResponse(exception, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({UserNotFoundException.class, ItemNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(RuntimeException exception) {
        return buildErrorResponse(exception, HttpStatus.NOT_FOUND);
    }

    public ApiError buildErrorResponse(RuntimeException exception, HttpStatus status) {
        return new ApiError(
                exception.getMessage(),
                status.value(),
                LocalDateTime.now()
        );
    }
}
