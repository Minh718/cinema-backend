package com.movie.seatservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie.seatservice.entities.BookingSeat;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {
}
