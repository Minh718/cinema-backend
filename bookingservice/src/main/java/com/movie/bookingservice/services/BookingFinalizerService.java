package com.movie.bookingservice.services;

import org.springframework.stereotype.Service;

import com.movie.bookingservice.dtos.BookingStatusTracker;
import com.movie.bookingservice.entities.Booking;
import com.movie.bookingservice.enums.BookingStatus;
import com.movie.bookingservice.repositories.BookingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingFinalizerService {

    private final BookingTracker bookingTracker;
    private final BookingRepository bookingRepository;

    public void tryFinalizeBooking(Long bookingId) {
        BookingStatusTracker status = bookingTracker.getStatus(bookingId);

        if (status.isSeatLocked() && status.isPaymentReady()) {
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking not found"));

            booking.setBookingStatus(BookingStatus.CONFIRMED);
            booking.setUrlPayment(status.getPaymentUrl());
            bookingRepository.save(booking);

            bookingTracker.clear(bookingId); // cleanup
        }
    }
}