package com.movie.showtimeservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie.showtimeservice.entities.ShowTime;

public interface ShowTimeRepository extends JpaRepository<ShowTime, Long> {
}