package com.movie.bookingservice.events.listeners;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.movie.bookingservice.constants.KafkaTopics;
import com.movie.bookingservice.events.models.SeatLockedEvent;
import com.movie.bookingservice.services.BookingService;

@Component
public class SeatEventListener {

    private final BookingService bookingService;

    public SeatEventListener(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @KafkaListener(topics = KafkaTopics.SEAT_LOCKED, groupId = "booking-service")
    public void handleSeatLocked(SeatLockedEvent event) {
        // bookingService.markAsConfirmed(event.getBookingId());
    }

    @KafkaListener(topics = KafkaTopics.SEAT_LOCK_FAILED, groupId = "booking-service")
    public void handleSeatLockFailed(SeatLockedEvent event) {
        // bookingService.cancelBooking(event.getBookingId());
    }
}