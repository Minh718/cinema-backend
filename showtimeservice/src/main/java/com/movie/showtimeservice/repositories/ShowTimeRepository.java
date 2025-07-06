package com.movie.showtimeservice.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie.showtimeservice.entities.ShowTime;

public interface ShowTimeRepository extends JpaRepository<ShowTime, Long> {

    List<ShowTime> findByRoomIdAndDateOrderByStartTimeAsc(Long roomId, LocalDate date);
}