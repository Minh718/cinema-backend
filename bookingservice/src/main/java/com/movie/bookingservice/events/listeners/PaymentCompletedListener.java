package com.movie.bookingservice.events.listeners;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.movie.bookingservice.events.models.PaymentCompletedEvent;
import com.movie.bookingservice.services.BookingService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentCompletedListener {

    private final BookingService bookingService;

    @KafkaListener(topics = "payment.completed", groupId = "booking-service")
    public void listen(PaymentCompletedEvent event) throws JsonProcessingException {
        bookingService.updateStatusBooking(event.getBookingId(),
                event.getStatus());
    }
}