package com.movie.bookingservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie.bookingservice.entities.BookingSeat;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {
}
