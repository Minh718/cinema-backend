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

        public Booking createBooking(BookingRequest request, String clientIp) throws JsonProcessingException {
                Booking booking = Booking.builder()
                                .userId(request.getUserId())
                                .showTimeId(request.getShowTimeId())
                                .totalPrice(request.getBookingInfoReq().getTotalPrice())
                                .paymentStatus(PaymentStatus.UNPAID)
                                .bookingStatus(BookingStatus.PENDING)
                                .build();

                Booking savedBooking = bookingRepository.save(booking);
                String orderInfo = objectMapper.writeValueAsString(request.getBookingInfoReq());
                BookingCreatedEvent event = BookingCreatedEvent.builder().roomId(request.getRoomId())
                                .showTimeId(request.getShowTimeId()).bookingId(booking.getId())
                                .seatIds(request.getSeatIds())
                                .amount(String.valueOf(request.getBookingInfoReq().getTotalPrice()))
                                .paymentMethod(request.getPaymentMethod()).orderId(String.valueOf(savedBooking.getId()))
                                .orderInfo(orderInfo).build();
                bookingEventPublisher.publishBookingCreatedEvent(event);

                return savedBooking;
        }

        public void updateBookingStatusAndPaymentStatus(Long bookingId, BookingStatus bookingStatus,
                        PaymentStatus paymentStatus)
                        throws JsonProcessingException {
                Booking booking = bookingRepository.findById(bookingId)
                                .orElseThrow(() -> new CustomException(ErrorCode.BOOKING_NOT_FOUND));
                booking.setPaymentStatus(paymentStatus);
                booking.setBookingStatus(bookingStatus);
                bookingRepository.save(booking);
        }
}