package com.project.shareitem.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String name;

    // TODO - email must be unique
    private String email;
}
