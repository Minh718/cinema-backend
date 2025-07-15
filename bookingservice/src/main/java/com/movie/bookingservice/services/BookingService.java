package com.movie.bookingservice.services;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.bookingservice.dtos.requests.BookingRequest;
import com.movie.bookingservice.dtos.requests.CreatePaymentReq;
import com.movie.bookingservice.dtos.responses.SeatResponse;
import com.movie.bookingservice.entities.Booking;
import com.movie.bookingservice.entities.BookingSeat;
import com.movie.bookingservice.enums.BookingStatus;
import com.movie.bookingservice.enums.PaymentStatus;
import com.movie.bookingservice.exceptions.CustomException;
import com.movie.bookingservice.exceptions.ErrorCode;
import com.movie.bookingservice.repositories.BookingRepository;
import com.movie.bookingservice.repositories.BookingSeatRepository;
import com.movie.bookingservice.repositories.httpClients.CinemaClient;
import com.movie.bookingservice.repositories.httpClients.PaymentClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {

        private final BookingRepository bookingRepository;
        private final BookingSeatRepository bookingSeatRepository;
        private final CinemaClient cinemaClient;
        private final RedisService redisService;
        private final PaymentClient paymentClient;
        private final ObjectMapper objectMapper;

        public Booking createBooking(BookingRequest request) throws JsonProcessingException {
                Map<Long, String> validSeatMap = cinemaClient.getSeatsByRoomId(request.getRoomId())
                                .getResult()
                                .stream()
                                .collect(Collectors.toMap(SeatResponse::getId, SeatResponse::getCode));

                List<Long> invalidSeats = request.getSeatIds().stream()
                                .filter(seatId -> !validSeatMap.containsKey(seatId))
                                .toList();

                if (!invalidSeats.isEmpty()) {
                        throw new CustomException(ErrorCode.SEAT_BOOKING_FAILED);
                }
                String redisKey = "showtime:" + request.getShowTimeId() + ":booked-seats";
                Set<Long> bookedSeatIds = redisService.getSeatIds(redisKey);
                List<Long> alreadyBooked = request.getSeatIds().stream()
                                .filter(bookedSeatIds::contains)
                                .toList();
                if (!alreadyBooked.isEmpty()) {
                        throw new CustomException(ErrorCode.SEAT_BOOKING_FAILED);
                }
                Booking booking = Booking.builder()
                                .userId(request.getUserId())
                                .showTimeId(request.getShowTimeId())
                                .totalPrice(request.getBookingInfoReq().getTotalPrice())
                                .paymentStatus(PaymentStatus.UNPAID)
                                .bookingStatus(BookingStatus.CONFIRMED)
                                .build();

                Booking savedBooking = bookingRepository.save(booking);
                String orderInfo = objectMapper.writeValueAsString(request.getBookingInfoReq());
                CreatePaymentReq createPaymentReq = CreatePaymentReq.builder()
                                .amount(String.valueOf(request.getBookingInfoReq().getTotalPrice()))
                                .paymentMethod(request.getPaymentMethod()).orderId(String.valueOf(savedBooking.getId()))
                                .orderInfo(orderInfo).build();
                String urlPayment = paymentClient.createPayment(createPaymentReq).getResult();

                savedBooking.setUrlPayment(urlPayment);
                Booking updateBooking = bookingRepository.save(savedBooking);
                redisService.addSet("payment:" + String.valueOf(updateBooking.getId()),
                                request.getSeatIds().toArray(new Long[0]));
                redisService.addSeats(redisKey, request.getSeatIds());

                List<BookingSeat> bookingSeats = request.getSeatIds().stream()
                                .map(seatId -> BookingSeat.builder()
                                                .seatId(seatId)
                                                .seatCode(validSeatMap.get(seatId))
                                                .booking(updateBooking)
                                                .build())
                                .toList();
                bookingSeatRepository.saveAll(bookingSeats);

                // List<String> seatStrings = request.getSeatIds().stream()
                // .map(String::valueOf)
                // .toList();
                // redisService.addSet(redisKey, seatStrings.toArray(new String[0]));
                // redisService.removeHeatSeats(redisKey, request.getSeatIds());
                return updateBooking;

        }

        public Set<Long> getBookedSeatIds(Long showTimeId) {
                String redisKey = "showtime:" + showTimeId + ":booked-seats";
                return redisService.getSeatIds(redisKey);
        }

        public Set<Long> getHeatSeatIds(Long showTimeId) {
                String redisKey = "showtime:" + showTimeId + ":heat-seats";
                return redisService.getSeatIds(redisKey);
        }

        public void updateStatusToPaidAndRedisSeatIds(Long bookingId, PaymentStatus status)
                        throws JsonProcessingException {
                Booking booking = bookingRepository.findById(bookingId)
                                .orElseThrow(() -> new CustomException(ErrorCode.BOOKING_NOT_FOUND));
                booking.setPaymentStatus(status);
                bookingRepository.save(booking);
                if (status == PaymentStatus.PAID) {
                        String redisKey = "payment:" + String.valueOf(bookingId);
                        redisService.delKey(redisKey);
                } else {
                        String keyUnpaidSeats = "payment:" + booking.getId();
                        String keyBookedSeats = "showtime:" + booking.getShowTimeId() + ":booked-seats";

                        Set<Long> unpaidSeats = redisService.getSeatIds(keyUnpaidSeats);
                        redisService.delKey(keyUnpaidSeats);
                        redisService.removeSeats(keyBookedSeats, unpaidSeats);
                }
        }
}