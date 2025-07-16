package com.movie.bookingservice.constants;

public class KafkaTopics {
    public static final String PAYMENT_COMPLETED = "payment.completed";
    public static final String PAYMENT_FAILED = "payment.failed";
    public static final String BOOK_CREATED = "booking.created";
    public static final String SEAT_LOCKED = "seat.locked";
    public static final String SEAT_LOCK_FAILED = "seat.lock.failed";
    public static final String URL_PAYMENT_CREATED = "url.payment.created";
    public static final String URL_PAYMENT_FAILED = "url.payment.failed";
}