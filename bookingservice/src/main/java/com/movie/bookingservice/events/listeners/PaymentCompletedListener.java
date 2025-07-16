package com.movie.bookingservice.events.listeners;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.movie.bookingservice.constants.KafkaTopics;
import com.movie.bookingservice.events.models.PaymentCompletedEvent;
import com.movie.bookingservice.events.models.PaymentFailedEvent;
import com.movie.bookingservice.services.BookingService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentCompletedListener {

    private final BookingService bookingService;

    @KafkaListener(topics = KafkaTopics.PAYMENT_COMPLETED, groupId = "booking-service")
    public void listen(PaymentCompletedEvent event) throws JsonProcessingException {
        bookingService.updateStatusBookingWhenPaymentCompleted(event.getBookingId());
    }

    @KafkaListener(topics = KafkaTopics.PAYMENT_FAILED, groupId = "booking-service")
    public void listen(PaymentFailedEvent event) throws JsonProcessingException {
        bookingService.updateStatusBookingWhenPaymentFailed(event.getBookingId());
    }
}