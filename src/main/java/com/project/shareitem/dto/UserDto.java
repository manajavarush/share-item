package com.project.shareitem.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    @NotBlank(message = "Имя не может быть пустым")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный email")
    @Column(nullable = false, unique = true)
    private String email;
}
