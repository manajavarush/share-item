package com.project.shareitem.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    // @Setter(AccessLevel.NONE)
    private Long id;

    private String name;

    private String email;
}
