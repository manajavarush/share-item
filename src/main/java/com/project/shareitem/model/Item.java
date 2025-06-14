package com.project.shareitem.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Setter(AccessLevel.NONE)
    private Long id;

    private String name;

    private String description;

    private boolean available;

    private User owner;

    private ItemRequest request;
}
