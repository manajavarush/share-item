package com.project.shareitem.dto;

import java.time.LocalDateTime;

public record ApiError(String message,
                       int statusCode,
                       LocalDateTime timestamp) {
}
