package com.project.shareitem.mapper;

import com.project.shareitem.dto.BookingCreateDto;
import com.project.shareitem.dto.BookingResponseDto;
import com.project.shareitem.entity.Booking;
import org.mapstruct.Mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    Booking toEntity(BookingCreateDto bookingCreateDto);

    BookingResponseDto toResponseDto(Booking booking);

    default List<BookingResponseDto> mapToDtos(List<Booking> bookings) {
        if (bookings == null || bookings.isEmpty()) {
            return Collections.emptyList();
        }

        return bookings.stream()
                .filter(Objects::nonNull)
                .map(this::toResponseDto)
                .toList();
    }
}
