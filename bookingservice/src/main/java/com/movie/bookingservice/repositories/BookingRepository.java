package com.movie.bookingservice.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie.bookingservice.entities.Booking;
import com.movie.bookingservice.enums.PaymentStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);

    List<Booking> findByStatusAndCreatedAtBefore(PaymentStatus paymentStatus, LocalDateTime createdAt);
}