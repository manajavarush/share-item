package com.project.shareitem.mapper;

import com.project.shareitem.dto.BookingCreateDto;
import com.project.shareitem.dto.BookingResponseDto;
import com.project.shareitem.entity.Booking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    Booking toEntity(BookingCreateDto bookingCreateDto);

    BookingResponseDto toResponseDto(Booking booking);
}
