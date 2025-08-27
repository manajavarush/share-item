package com.project.shareitem.handler;

import com.project.shareitem.dto.ApiError;
import com.project.shareitem.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleEmailExists(EmailAlreadyExistsException exception) {
        return buildErrorResponse(exception, HttpStatus.CONFLICT);
    }


    @ExceptionHandler({ItemAccessDeniedException.class, BookingAccessDeniedException.class, UserNotOwnerException.class})
    public ResponseEntity<ApiError> handleAccessDenied(ItemAccessDeniedException exception) {
        return buildErrorResponse(exception, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({UserNotFoundException.class, ItemNotFoundException.class, BookingNotFoundException.class})
    public ResponseEntity<ApiError> handleNotFound(RuntimeException exception) {
        return buildErrorResponse(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ItemNotAvailableException.class)
    public ResponseEntity<ApiError> handleItemNotAvailable(ItemNotAvailableException exception) {
        return buildErrorResponse(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidBookingTimeException.class)
    public ResponseEntity<ApiError> handleInvalidBookingTime(InvalidBookingTimeException exception) {
        return buildErrorResponse(exception, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<ApiError> buildErrorResponse(RuntimeException exception, HttpStatus status) {
        var error = new ApiError(
                exception.getMessage(),
                status.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error, status);
    }
}
