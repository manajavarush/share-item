package com.project.shareitem.dto;

public record ItemResponseDto(Long id,
                              String name,
                              String description,
                              Boolean available,
                              Long ownerId) {
}
