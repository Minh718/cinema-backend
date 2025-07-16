package com.movie.seatservice.events.listeners;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.movie.seatservice.constants.KafkaTopics;
import com.movie.seatservice.events.models.PaymentCompletedEvent;
import com.movie.seatservice.events.models.PaymentFailedEvent;
import com.movie.seatservice.services.SeatService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final SeatService seatService;

    @KafkaListener(topics = KafkaTopics.PAYMENT_COMPLETED, groupId = "seat-service")
    public void listen(PaymentCompletedEvent event) throws JsonProcessingException {
        seatService.processingWhenPaymentCompleted(event.getBookingId());
    }

    @KafkaListener(topics = KafkaTopics.PAYMENT_FAILED, groupId = "seat-service")
    public void listen(PaymentFailedEvent event) throws JsonProcessingException {
        seatService.processingWhenPaymentFailed(event.getBookingId());
    }
}