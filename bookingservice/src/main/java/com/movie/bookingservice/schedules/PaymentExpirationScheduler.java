package com.movie.bookingservice.schedules;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;

import com.movie.bookingservice.entities.Booking;
import com.movie.bookingservice.enums.PaymentStatus;
import com.movie.bookingservice.repositories.BookingRepository;
import com.movie.bookingservice.services.RedisService;

import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

@RequiredArgsConstructor
public class PaymentExpirationScheduler {
    private final BookingRepository bookingRepository;
    private final RedisService redisService;

    @SchedulerLock(name = "markExpiredPayments", lockAtMostFor = "PT5M")
    @Scheduled(fixedRate = 300_000) // every 5 minutes
    public void markExpiredPayments() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(15);
        List<Booking> expiredBookings = bookingRepository.findByStatusAndCreatedAtBefore(PaymentStatus.UNPAID, cutoff);

        for (Booking booking : expiredBookings) {
            booking.setPaymentStatus(PaymentStatus.FAILED);

            String keyUnpaidSeats = "payment:" + booking.getId();
            String keyBookedSeats = "showtime:" + booking.getShowTimeId() + ":booked-seats";

            Set<Long> unpaidSeats = redisService.getSeatIds(keyUnpaidSeats);
            redisService.delKey(keyUnpaidSeats);
            redisService.removeSeats(keyBookedSeats, unpaidSeats);

            bookingRepository.save(booking);
        }
    }

}