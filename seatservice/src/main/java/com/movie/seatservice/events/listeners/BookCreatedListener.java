package com.movie.seatservice.events.listeners;

import java.time.Instant;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.movie.seatservice.constants.KafkaTopics;
import com.movie.seatservice.events.models.BookingCreatedEvent;
import com.movie.seatservice.events.models.SeatLockFailedEvent;
import com.movie.seatservice.events.models.SeatLockedEvent;
import com.movie.seatservice.services.SeatService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookCreatedListener {

    private final SeatService seatService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = KafkaTopics.BOOK_CREATED, groupId = "seat-service")
    public void listen(BookingCreatedEvent event) throws JsonProcessingException {
        try {
            seatService.validateAndLockSeats(event);
            kafkaTemplate.send(KafkaTopics.SEAT_LOCKED,
                    new SeatLockedEvent(event.getBookingId(), "success", Instant.now()).setSeatIds(event.getSeatIds()));
        } catch (Exception e) {
            kafkaTemplate.send(KafkaTopics.SEAT_LOCK_FAILED,
                    new SeatLockFailedEvent(event.getBookingId(), e.getMessage(), Instant.now()));
        }
    }
}