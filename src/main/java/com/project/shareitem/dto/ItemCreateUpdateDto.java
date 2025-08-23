package com.project.shareitem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ItemCreateUpdateDto(@NotBlank(message = "Название вещи не может быть пустым")
                                  String name,
                                  @NotBlank(message = "Описание вещи не может быть пустым")
                                  String description,
                                  @NotNull(message = "Статус вещи не может быть пустым")
                                  Boolean available) {
}
