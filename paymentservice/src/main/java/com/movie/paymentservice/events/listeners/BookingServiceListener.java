
package com.movie.paymentservice.events.listeners;

import java.time.Instant;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.movie.paymentservice.constants.KafkaTopics;
import com.movie.paymentservice.events.models.BookingCreatedEvent;
import com.movie.paymentservice.events.models.UrlPaymentCreatedEvent;
import com.movie.paymentservice.events.models.UrlPaymentFailedEvent;
import com.movie.paymentservice.services.PaymentService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookingServiceListener {

    private final PaymentService paymentService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = KafkaTopics.BOOK_CREATED, groupId = "payment-service")
    public void listen(BookingCreatedEvent event) throws JsonProcessingException {
        try {
            String urlPayment = paymentService.createPayment(event);
            kafkaTemplate.send(KafkaTopics.URL_PAYMENT_CREATED,
                    new UrlPaymentCreatedEvent(event.getBookingId(), "success", Instant.now(), urlPayment));
        } catch (Exception e) {
            kafkaTemplate.send(KafkaTopics.URL_PAYMENT_FAILED,
                    new UrlPaymentFailedEvent(event.getBookingId(), e.getMessage(), Instant.now()));
        }
    }
}