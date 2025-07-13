package com.movie.bookingservice.services;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.movie.bookingservice.dtos.requests.BookingRequest;
import com.movie.bookingservice.dtos.responses.SeatResponse;
import com.movie.bookingservice.entities.Booking;
import com.movie.bookingservice.entities.BookingSeat;
import com.movie.bookingservice.exceptions.CustomException;
import com.movie.bookingservice.exceptions.ErrorCode;
import com.movie.bookingservice.repositories.BookingRepository;
import com.movie.bookingservice.repositories.BookingSeatRepository;
import com.movie.bookingservice.repositories.httpClients.CinemaClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {

        private final BookingRepository bookingRepository;
        private final BookingSeatRepository bookingSeatRepository;
        private final CinemaClient cinemaClient;
        private final RedisService redisService;

        public Booking createBooking(BookingRequest request) {
                Booking booking = Booking.builder()
                                .userId(request.getUserId())
                                .showTimeId(request.getShowTimeId())
                                .totalPrice(0.0)
                                .paymentStatus("UNPAID")
                                .bookingStatus("CONFIRMED")
                                .build();

                Booking saved = bookingRepository.save(booking);

                List<SeatResponse> allSeats = cinemaClient.getSeatsByRoomId(request.getRoomId()).getResult();
                Map<Long, String> validSeatMap = allSeats.stream()
                                .collect(Collectors.toMap(SeatResponse::getId, SeatResponse::getCode));

                List<Long> invalidSeats = request.getSeatIds().stream()
                                .filter(seatId -> !validSeatMap.containsKey(seatId))
                                .toList();

                if (!invalidSeats.isEmpty()) {
                        throw new CustomException(ErrorCode.SEAT_BOOKING_FAILED);
                }
                String redisKey = "showtime:" + request.getShowTimeId() + ":booked-seats";
                Set<Object> bookedSeats = redisService.getSet(redisKey);
                Set<Long> bookedSeatIds = bookedSeats.stream()
                                .map(Object::toString) // safely convert each Object to String
                                .map(Long::valueOf) // then parse to Long
                                .collect(Collectors.toSet());
                List<Long> alreadyBooked = request.getSeatIds().stream()
                                .filter(bookedSeatIds::contains)
                                .collect(Collectors.toList());

                if (!alreadyBooked.isEmpty()) {
                        throw new CustomException(ErrorCode.SEAT_BOOKING_FAILED);
                }

                List<BookingSeat> bookingSeats = request.getSeatIds().stream()
                                .map(seatId -> BookingSeat.builder()
                                                .seatId(seatId)
                                                .seatCode(validSeatMap.get(seatId))
                                                .booking(saved)
                                                .build())
                                .toList();
                bookingSeatRepository.saveAll(bookingSeats);
                List<String> seatStrings = request.getSeatIds().stream()
                                .map(String::valueOf)
                                .toList();
                redisService.addSet(redisKey, seatStrings.toArray(new String[0]));
                return saved;

        }

}