package com.project.shareitem.dto;

import java.time.LocalDateTime;

public record BookingCreateDto(Long itemId,
                               LocalDateTime start,
                               LocalDateTime end) {
}
