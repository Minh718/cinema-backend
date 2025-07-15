package com.movie.seatservice.events.listeners;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.movie.seatservice.events.models.BookingCreatedEvent;
import com.movie.seatservice.services.SeatService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookCreatedListener {

    private final SeatService seatService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "booking.created", groupId = "seat-service")
    public void listen(BookingCreatedEvent event) throws JsonProcessingException {
        try {
            seatService.validateAndLockSeats(event);
            kafkaTemplate.send("seat.locked", event.getBookingId());
        } catch (Exception e) {
            kafkaTemplate.send("seat.lock.failed",
                    event.getBookingId());
        }
    }
}