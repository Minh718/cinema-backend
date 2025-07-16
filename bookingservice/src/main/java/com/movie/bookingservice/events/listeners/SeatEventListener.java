package com.movie.bookingservice.events.listeners;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.movie.bookingservice.constants.KafkaTopics;
import com.movie.bookingservice.enums.BookingStatus;
import com.movie.bookingservice.enums.PaymentStatus;
import com.movie.bookingservice.events.models.SeatLockedEvent;
import com.movie.bookingservice.services.BookingFinalizerService;
import com.movie.bookingservice.services.BookingService;
import com.movie.bookingservice.services.BookingTracker;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SeatEventListener {

    private final BookingService bookingService;
    private final BookingTracker bookingTracker;
    private final BookingFinalizerService finalizerService;

    @KafkaListener(topics = KafkaTopics.SEAT_LOCKED, groupId = "booking-service")
    public void handleSeatLocked(SeatLockedEvent event) {
        bookingTracker.markSeatLocked(event.getBookingId(), event.getSeatIds());
        finalizerService.tryFinalizeBooking(event.getBookingId());
    }

    @KafkaListener(topics = KafkaTopics.SEAT_LOCK_FAILED, groupId = "booking-service")
    public void handleSeatLockFailed(SeatLockedEvent event) throws JsonProcessingException {
        bookingService.updateBookingStatusAndPaymentStatus(event.getBookingId(), BookingStatus.FAILED,
                PaymentStatus.FAILED);
    }
}