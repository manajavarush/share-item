package com.project.shareitem.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseDto {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private Long ownerId;
}
