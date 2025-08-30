package com.project.shareitem.enums;

public enum BookingStateRequest {
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED,
    ALL;

    public static BookingStateRequest from(String value) {
        try {
            return valueOf(value.toUpperCase());
        } catch (IllegalStateException exception) {
            return ALL;
        }
    }
}
