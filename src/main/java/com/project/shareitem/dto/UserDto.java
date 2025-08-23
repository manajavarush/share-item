package com.project.shareitem.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDto(@NotBlank(message = "Имя не может быть пустым")
                      @Column(nullable = false)
                      String name,
                      @NotBlank(message = "Email не может быть пустым")
                      @Email(message = "Некорректный email")
                      @Column(nullable = false, unique = true)
                      String email) {
}
