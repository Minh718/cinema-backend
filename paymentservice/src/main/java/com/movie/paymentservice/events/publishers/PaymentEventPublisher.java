package com.movie.paymentservice.events.publishers;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.movie.paymentservice.events.models.PaymentCompletedEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "payment.completed";

    public void publishPaymentCompletedEvent(PaymentCompletedEvent event) {
        kafkaTemplate.send(TOPIC, event);
    }
}