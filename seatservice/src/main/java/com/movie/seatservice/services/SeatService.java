package com.movie.seatservice.services;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.movie.seatservice.dtos.requests.SeatReq;
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
        String redisKey = getShowtimeBookedSeatsKey(String.valueOf(showTimeId));
        return redisService.getSeatIds(redisKey);
    }

    public Set<Long> getHeatSeatIds(Long showTimeId) {
        String redisKey = getShowtimeHeatSeatsKey(String.valueOf(showTimeId));
        return redisService.getSeatIds(redisKey);
    }

    public void validateAndLockSeats(BookingCreatedEvent event) {
        Long bookingId = event.getBookingId();
        Long showTimeId = event.getShowTimeId();
        String redisKey = getShowtimeBookedSeatsKey(String.valueOf(showTimeId));

        Map<Long, String> validSeatMap = cinemaClient.getSeatsByRoomId(event.getRoomId())
                .getResult()
                .stream()
                .collect(Collectors.toMap(SeatResponse::getId, SeatResponse::getCode));

        List<SeatReq> invalidSeats = event.getSeats().stream()
                .filter(seat -> {
                    String validCode = validSeatMap.get(seat.getId());
                    return validCode == null || !validCode.equals(seat.getCode());
                })
                .toList();

        if (!invalidSeats.isEmpty()) {
            throw new CustomException(ErrorCode.SEAT_BOOKING_FAILED);
        }

        Set<Long> bookedSeatIds = redisService.getSeatIds(redisKey);
        Set<Long> bookingSeatIds = event.getSeats().stream()
                .map(SeatReq::getId)
                .collect(Collectors.toSet());
        List<Long> alreadyBooked = bookingSeatIds.stream()
                .filter(bookedSeatIds::contains)
                .toList();

        if (!alreadyBooked.isEmpty()) {
            throw new CustomException(ErrorCode.SEAT_BOOKING_FAILED);
        }

        redisService.setWithTTL(getUnpaidSeatsKey(bookingId), bookingSeatIds.toArray(Long[]::new), 15,
                TimeUnit.MINUTES);
        redisService.setKeyinMinutes(getBookingShowTimeKey(bookingId), showTimeId, 15);

        redisService.addSeats(redisKey, bookingSeatIds);

        List<BookingSeat> bookingSeats = bookingSeatIds.stream()
                .map(seatId -> BookingSeat.builder()
                        .bookingId(bookingId)
                        .seatId(seatId)
                        .seatCode(validSeatMap.get(seatId))
                        .build())
                .toList();

        bookingSeatRepository.saveAll(bookingSeats);
    }

    public void processingWhenPaymentCompleted(Long bookingId) {
        String redisKey = getUnpaidSeatsKey(bookingId);
        ;
        redisService.delKey(redisKey);
    }

    public void processingWhenPaymentFailed(Long bookingId) {
        String keyUnpaidSeats = getUnpaidSeatsKey(bookingId);
        String showTimeId = redisService.getKey(getBookingShowTimeKey(bookingId)).toString();
        String keyBookedSeats = getShowtimeBookedSeatsKey(showTimeId);

        Set<Long> unpaidSeats = redisService.getSeatIds(keyUnpaidSeats);
        redisService.delKey(keyUnpaidSeats);
        redisService.removeSeats(keyBookedSeats, unpaidSeats);
    }

    private String getUnpaidSeatsKey(Long bookingId) {
        return "payment:" + bookingId + ":seats";
    }

    private String getBookingShowTimeKey(Long bookingId) {
        return "booking:" + bookingId + ":showTimeId";
    }

    private String getShowtimeBookedSeatsKey(String showTimeId) {
        return "showtime:" + showTimeId + ":booked-seats";
    }

    private String getShowtimeHeatSeatsKey(String showTimeId) {
        return "showtime:" + showTimeId + ":heat-seats";
    }
}