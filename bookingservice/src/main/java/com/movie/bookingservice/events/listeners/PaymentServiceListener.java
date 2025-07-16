package com.movie.bookingservice.events.listeners;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.movie.bookingservice.constants.KafkaTopics;
import com.movie.bookingservice.enums.BookingStatus;
import com.movie.bookingservice.enums.PaymentStatus;
import com.movie.bookingservice.events.models.PaymentCompletedEvent;
import com.movie.bookingservice.events.models.PaymentFailedEvent;
import com.movie.bookingservice.events.models.UrlPaymentCreatedEvent;
import com.movie.bookingservice.events.models.UrlPaymentFailedEvent;
import com.movie.bookingservice.services.BookingFinalizerService;
import com.movie.bookingservice.services.BookingService;
import com.movie.bookingservice.services.BookingTracker;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentServiceListener {

    private final BookingService bookingService;
    private final BookingTracker bookingTracker;
    private final BookingFinalizerService finalizerService;

    @KafkaListener(topics = KafkaTopics.PAYMENT_COMPLETED, groupId = "booking-service")
    public void listen(PaymentCompletedEvent event) throws JsonProcessingException {
        bookingService.updateBookingStatusAndPaymentStatus(event.getBookingId(), BookingStatus.CONFIRMED,
                PaymentStatus.PAID);
    }

    @KafkaListener(topics = KafkaTopics.PAYMENT_FAILED, groupId = "booking-service")
    public void listen(PaymentFailedEvent event) throws JsonProcessingException {
        bookingService.updateBookingStatusAndPaymentStatus(event.getBookingId(), BookingStatus.FAILED,
                PaymentStatus.FAILED);
    }

    @KafkaListener(topics = KafkaTopics.URL_PAYMENT_CREATED, groupId = "booking-service")
    public void listen(UrlPaymentCreatedEvent event) throws JsonProcessingException {
        bookingTracker.markPaymentReady(event.getBookingId(), event.getUrlPayment());
        finalizerService.tryFinalizeBooking(event.getBookingId());
    }

    @KafkaListener(topics = KafkaTopics.URL_PAYMENT_FAILED, groupId = "booking-service")
    public void listen(UrlPaymentFailedEvent event) throws JsonProcessingException {
        bookingService.updateBookingStatusAndPaymentStatus(event.getBookingId(), BookingStatus.FAILED,
                PaymentStatus.FAILED);
    }

}