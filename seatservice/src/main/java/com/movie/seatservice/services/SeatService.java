package com.movie.seatservice.services;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.movie.seatservice.dtos.responses.SeatResponse;
import com.movie.seatservice.entities.BookingSeat;
import com.movie.seatservice.events.models.BookingCreatedEvent;
import com.movie.seatservice.exceptions.CustomException;
import com.movie.seatservice.exceptions.ErrorCode;
import com.movie.seatservice.repositories.BookingSeatRepository;
import com.movie.seatservice.repositories.httpClients.CinemaClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class SeatService {
    private final RedisService redisService;
    private final CinemaClient cinemaClient;
    private final BookingSeatRepository bookingSeatRepository;

    public Set<Long> getBookedSeatIds(Long showTimeId) {
        String redisKey = "showtime:" + showTimeId + ":booked-seats";
        return redisService.getSeatIds(redisKey);
    }

    public Set<Long> getHeatSeatIds(Long showTimeId) {
        String redisKey = "showtime:" + showTimeId + ":heat-seats";
        return redisService.getSeatIds(redisKey);
    }

    public void validateAndLockSeats(BookingCreatedEvent event) {
        Long bookingId = event.getBookingId();
        Long showTimeId = event.getShowTimeId();
        String redisKey = "showtime:" + showTimeId + ":booked-seats";

        Map<Long, String> validSeatMap = cinemaClient.getSeatsByRoomId(event.getRoomId())
                .getResult()
                .stream()
                .collect(Collectors.toMap(SeatResponse::getId, SeatResponse::getCode));

        List<Long> invalidSeatIds = event.getSeatIds().stream()
                .filter(seatId -> !validSeatMap.containsKey(seatId))
                .toList();

        if (!invalidSeatIds.isEmpty()) {
            throw new CustomException(ErrorCode.SEAT_BOOKING_FAILED);
        }

        Set<Long> bookedSeatIds = redisService.getSeatIds(redisKey);
        List<Long> alreadyBooked = event.getSeatIds().stream()
                .filter(bookedSeatIds::contains)
                .toList();

        if (!alreadyBooked.isEmpty()) {
            throw new CustomException(ErrorCode.SEAT_BOOKING_FAILED);
        }

        redisService.setWithTTL("booking:" + bookingId + ":seats", event.getSeatIds().toArray(Long[]::new), 15,
                TimeUnit.MINUTES);
        redisService.setKeyinMinutes("booking:" + bookingId + ":showTimeId", showTimeId, 15);

        redisService.addSeats(redisKey, event.getSeatIds());

        List<BookingSeat> bookingSeats = event.getSeatIds().stream()
                .map(seatId -> BookingSeat.builder()
                        .bookingId(bookingId)
                        .seatId(seatId)
                        .seatCode(validSeatMap.get(seatId))
                        .build())
                .toList();

        bookingSeatRepository.saveAll(bookingSeats);
    }
}