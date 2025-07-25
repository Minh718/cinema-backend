package com.movie.bookingservice.events.publishers;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.movie.bookingservice.constants.KafkaTopics;
import com.movie.bookingservice.events.models.BookingCreatedEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingEventPublisher {

    private final KafkaTemplate<String, BookingCreatedEvent> kafkaTemplate;

    public void publishBookingCreatedEvent(BookingCreatedEvent event) {
        kafkaTemplate.send(KafkaTopics.BOOK_CREATED, event);
    }
}