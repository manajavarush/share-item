package com.project.shareitem.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {

    @Setter(AccessLevel.NONE)
    private Long id;

    private String description;

    private User requestor;

    private LocalDateTime created;
}
