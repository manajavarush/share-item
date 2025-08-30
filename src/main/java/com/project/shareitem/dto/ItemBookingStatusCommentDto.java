package com.project.shareitem.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ItemBookingStatusCommentDto(Long id,
                                          String name,
                                          String description,
                                          Boolean available,
                                          LocalDateTime lastBooking,
                                          LocalDateTime nextBooking,
                                          List<CommentDto> comments) {
}
