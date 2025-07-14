package com.movie.notificationservice.enums;

public enum MailType {
    BOOKING_CONFIRMATION("Your Movie Booking is Confirmed 🎉"),
    SHOWTIME_REMINDER("Reminder: Your Movie Starts Soon 🎬"),
    BOOKING_CANCELLATION("Your Booking Has Been Cancelled ❌"),
    PROMOTION("Don't Miss Out! Exclusive Cinema Deals 🍿"),
    EMAIL_VERIFICATION("Verify Your Email to Complete Registration ✅");

    private final String subject;

    MailType(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }
}
