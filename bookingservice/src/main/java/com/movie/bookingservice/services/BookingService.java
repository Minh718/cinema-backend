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
import com.movie.bookingservice.enums.BookingStatus;
import com.movie.bookingservice.enums.PaymentStatus;
import com.movie.bookingservice.events.models.BookingCreatedEvent;
import com.movie.bookingservice.events.publishers.BookingEventPublisher;
import com.movie.bookingservice.exceptions.CustomException;
import com.movie.bookingservice.exceptions.ErrorCode;
import com.movie.bookingservice.repositories.BookingRepository;
import com.movie.bookingservice.repositories.httpClients.CinemaClient;
import com.movie.bookingservice.repositories.httpClients.PaymentClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {

        private final BookingRepository bookingRepository;
        private final BookingEventPublisher bookingEventPublisher;
        private final RedisService redisService;
        private final PaymentClient paymentClient;
        private final ObjectMapper objectMapper;

        public Booking createBooking(BookingRequest request) throws JsonProcessingException {
                Booking booking = Booking.builder()
                                .userId(request.getUserId())
                                .showTimeId(request.getShowTimeId())
                                .totalPrice(request.getBookingInfoReq().getTotalPrice())
                                .paymentStatus(PaymentStatus.UNPAID)
                                .bookingStatus(BookingStatus.PENDING)
                                .build();

                Booking savedBooking = bookingRepository.save(booking);
                BookingCreatedEvent event = BookingCreatedEvent.builder().roomId(request.getRoomId())
                                .showTimeId(request.getShowTimeId()).bookingId(booking.getId())
                                .seatIds(request.getSeatIds()).build();
                bookingEventPublisher.publishBookingCreatedEvent(event);

                String orderInfo = objectMapper.writeValueAsString(request.getBookingInfoReq());
                CreatePaymentReq createPaymentReq = CreatePaymentReq.builder()
                                .amount(String.valueOf(request.getBookingInfoReq().getTotalPrice()))
                                .paymentMethod(request.getPaymentMethod()).orderId(String.valueOf(savedBooking.getId()))
                                .orderInfo(orderInfo).build();
                String urlPayment = paymentClient.createPayment(createPaymentReq).getResult();

                savedBooking.setUrlPayment(urlPayment);
                Booking updateBooking = bookingRepository.save(savedBooking);
                return updateBooking;
        }

        public void updateStatusBooking(Long bookingId, PaymentStatus status)
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