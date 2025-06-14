package com.project.shareitem.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Setter(AccessLevel.NONE)
    private Long id;

    private LocalDateTime start;
    private LocalDateTime end;

    private Item item;

    private User booker;

    private BookingStatus status;
}
