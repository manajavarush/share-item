package com.project.shareitem.controller;

import com.project.shareitem.dto.BookingCreateDto;
import com.project.shareitem.dto.BookingResponseDto;
import com.project.shareitem.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(@RequestBody BookingCreateDto bookingDto,
                                                            @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        var newBooking = bookingService.createBooking(bookingDto, userId);

        return ResponseEntity.ok(newBooking);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingResponseDto> approveBooking(@PathVariable Long bookingId,
                                                             @RequestParam Boolean approved,
                                                             @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        var approvedBooking = bookingService.approveBooking(bookingId, approved, userId);

        return ResponseEntity.ok(approvedBooking);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponseDto> getBookingById(@PathVariable Long bookingId,
                                                             @RequestHeader(value = "X-Sharer-User-Id") Long userId) {

        var bookingResponseDto = bookingService.getBookingById(bookingId, userId);

        return ResponseEntity.ok(bookingResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDto>> getUserBookings
            (@RequestParam(defaultValue = "ALL") String state,
             @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        var bookingResponseDtos = bookingService.getUserBookings(state, userId);

        return ResponseEntity.ok(bookingResponseDtos);
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingResponseDto>> getOwnerBookings
            (@RequestParam(defaultValue = "ALL") String state,
             @RequestHeader(value = "X-Sharer-User-Id") Long ownerId) {
        var bookingResponseDtos = bookingService.getOwnerBookings(state, ownerId);

        return ResponseEntity.ok(bookingResponseDtos);
    }
}
