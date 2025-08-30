package com.project.shareitem.dto;

import com.project.shareitem.enums.BookingStatus;

import java.time.LocalDateTime;

public record BookingResponseDto(Long id,
                                 LocalDateTime start,
                                 LocalDateTime end,
                                 BookingStatus status,
                                 UserShortDto booker,
                                 ItemShortDto item) {
}
