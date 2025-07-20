package com.movie.bookingservice.services;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.movie.bookingservice.dtos.BookingStatusTracker;
import com.movie.bookingservice.dtos.requests.SeatReq;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingTracker {
    private final RedisService redisService;
    private static final Duration TTL = Duration.ofMinutes(10);

    private String key(Long bookingId) {
        return "booking:status:" + bookingId;
    }

    public BookingStatusTracker getStatus(Long bookingId) {
        Object value = redisService.getKey(key(bookingId));
        return value != null ? (BookingStatusTracker) value : new BookingStatusTracker();
    }

    public void markSeatLocked(Long bookingId) {
        BookingStatusTracker status = getStatus(bookingId);
        status.setSeatLocked(true);
        status.setBookingId(bookingId);
        redisService.setKeyinDuration(key(bookingId), status, TTL);
    }

    public void markPaymentReady(Long bookingId, String url) {
        BookingStatusTracker status = getStatus(bookingId);
        status.setPaymentReady(true);
        status.setPaymentUrl(url);
        status.setBookingId(bookingId);
        redisService.setKeyinDuration(key(bookingId), status, TTL);
    }

    public void clear(Long bookingId) {
        redisService.delKey(key(bookingId));
    }
}