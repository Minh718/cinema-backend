package com.movie.bookingservice.enums;

public enum BookingStatus {
    PENDING, // Booking created, waiting for seat lock & payment

    CONFIRMED, // Seats locked, payment URL generated

    CANCELED, // Cancelled due to timeout, failure, or user action

    FAILED // Generic failure (seat lock or payment failed)
}