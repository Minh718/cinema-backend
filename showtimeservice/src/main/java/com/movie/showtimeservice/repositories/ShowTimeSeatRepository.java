package com.movie.showtimeservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie.showtimeservice.entities.ShowTimeSeat;

public interface ShowTimeSeatRepository extends JpaRepository<ShowTimeSeat, Long> {
}
