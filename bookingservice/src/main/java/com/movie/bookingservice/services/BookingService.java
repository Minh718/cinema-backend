package com.movie.bookingservice.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.bookingservice.dtos.requests.BookingInfoReq;
import com.movie.bookingservice.dtos.requests.BookingRequest;
import com.movie.bookingservice.dtos.requests.SeatReq;
import com.movie.bookingservice.dtos.responses.CinemaRes;
import com.movie.bookingservice.dtos.responses.MovieNameRes;
import com.movie.bookingservice.dtos.responses.ShowTimeRes;
import com.movie.bookingservice.dtos.responses.UserInfoResponse;
import com.movie.bookingservice.entities.Booking;
import com.movie.bookingservice.enums.BookingStatus;
import com.movie.bookingservice.enums.PaymentStatus;
import com.movie.bookingservice.events.models.BookingCreatedEvent;
import com.movie.bookingservice.events.publishers.BookingEventPublisher;
import com.movie.bookingservice.exceptions.CustomException;
import com.movie.bookingservice.exceptions.ErrorCode;
import com.movie.bookingservice.repositories.BookingRepository;
import com.movie.bookingservice.repositories.KeycloakUserInfoClient;
import com.movie.bookingservice.repositories.httpClients.CinemaClient;
import com.movie.bookingservice.repositories.httpClients.MovieClient;
import com.movie.bookingservice.repositories.httpClients.PaymentClient;
import com.movie.bookingservice.repositories.httpClients.ShowTimeClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {

        private final BookingRepository bookingRepository;
        private final BookingEventPublisher bookingEventPublisher;
        private final KeycloakUserInfoClient keycloakUserInfoClient;
        private final RedisService redisService;
        private final CinemaClient cinemaClient;
        private final MovieClient movieClient;
        private final PaymentClient paymentClient;
        private final ObjectMapper objectMapper;
        private final ShowTimeClient showtimeClient;

        @Value("${keycloak.realm}")
        private String realm;

        public Booking createBooking(BookingRequest request, String clientIp, String tokenBearer, String userId)
                        throws JsonProcessingException {
                try {
                        ShowTimeRes showTimeRes = showtimeClient.checkShowTimeAvailable(request.getShowTimeId())
                                        .getResult();
                        UserInfoResponse userInfoResponse = fetchUserInfo(tokenBearer, userId);
                        CinemaRes cinemaRes = getCinemaById(showTimeRes.getCinemaId());
                        MovieNameRes movieRes = getmovieById(showTimeRes.getMovieId());
                        if (showTimeRes.getBasePrice() * request.getSeats().size() != request
                                        .getTotalPrice()) {
                                throw new CustomException(ErrorCode.PRICE_MISMATCH);
                        }

                        Booking booking = Booking.builder()
                                        .userId(userInfoResponse.getSub())
                                        .showTimeId(request.getShowTimeId())
                                        .totalPrice(request.getTotalPrice())
                                        .paymentStatus(PaymentStatus.UNPAID)
                                        .bookingStatus(BookingStatus.PENDING)
                                        .build();

                        Booking savedBooking = bookingRepository.save(booking);
                        BookingInfoReq bookingInfoReq = BookingInfoReq.builder().bookingId(savedBooking.getId())
                                        .cinemaName(cinemaRes.getName()).movieTitle(movieRes.getTitle())
                                        .showTime(formatShowTime(showTimeRes.getDate(), showTimeRes.getStartTime(),
                                                        showTimeRes.getEndTime()))
                                        .phone(userInfoResponse.getPhone_number()).email(userInfoResponse.getEmail())
                                        .totalPrice(request.getTotalPrice())
                                        .seatNumbers(getSeatCodesString(request.getSeats()))
                                        .customerName(userInfoResponse.getName()).build();

                        String orderInfo = objectMapper.writeValueAsString(bookingInfoReq);
                        BookingCreatedEvent event = BookingCreatedEvent.builder().roomId(showTimeRes.getRoomId())
                                        .showTimeId(request.getShowTimeId()).bookingId(booking.getId())
                                        .seats(request.getSeats())
                                        .amount(String.valueOf(request.getTotalPrice()))
                                        .paymentMethod(request.getPaymentMethod())
                                        .orderId(String.valueOf(savedBooking.getId()))
                                        .orderInfo(orderInfo).build();
                        bookingEventPublisher.publishBookingCreatedEvent(event);

                        return savedBooking;
                } catch (Exception e) {
                        throw new CustomException(ErrorCode.SEAT_BOOKING_FAILED);
                }
        }

        public String getSeatCodesString(Set<SeatReq> seats) {
                return seats.stream()
                                .map(SeatReq::getCode)
                                .collect(Collectors.joining(", "));
        }

        public String formatShowTime(LocalDate date, LocalTime startTime, LocalTime endTime) {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

                return String.format("%s | %s - %s",
                                date.format(dateFormatter),
                                startTime.format(timeFormatter),
                                endTime.format(timeFormatter));
        }

        @Cacheable(value = "userInfo", key = "#userId")
        public UserInfoResponse fetchUserInfo(String bearerToken, String userId) {
                UserInfoResponse userInfo = keycloakUserInfoClient.getUserInfo(bearerToken, realm);
                return userInfo;
        }

        @Cacheable(value = "cinema", key = "#cinemaId")
        public CinemaRes getCinemaById(Long cinemaId) {
                return cinemaClient.getDetailCinema(cinemaId).getResult();
        }

        @Cacheable(value = "movie", key = "#movieId")
        public MovieNameRes getmovieById(Long movieId) {
                return movieClient.getNameMovie(movieId).getResult();
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