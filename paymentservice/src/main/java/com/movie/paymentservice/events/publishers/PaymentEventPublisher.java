package com.movie.paymentservice.events.publishers;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.movie.paymentservice.constants.KafkaTopics;
import com.movie.paymentservice.events.models.PaymentCompletedEvent;
import com.movie.paymentservice.events.models.PaymentFailedEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishPaymentCompletedEvent(PaymentCompletedEvent event) {
        kafkaTemplate.send(KafkaTopics.PAYMENT_COMPLETED, event);
    }

    public void publishPaymentFailedEvent(PaymentFailedEvent event) {
        kafkaTemplate.send(KafkaTopics.PAYMENT_FAILED, event);
    }
}