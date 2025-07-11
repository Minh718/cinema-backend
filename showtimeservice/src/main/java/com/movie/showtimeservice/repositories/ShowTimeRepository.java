package com.movie.showtimeservice.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie.showtimeservice.entities.ShowTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.movie.showtimeservice.enums.ShowTimeStatus;

public interface ShowTimeRepository extends JpaRepository<ShowTime, Long> {

    List<ShowTime> findByRoomIdAndDateOrderByStartTimeAsc(Long roomId, LocalDate date);

    @Query("SELECT DISTINCT s.date FROM ShowTime s " +
            "WHERE s.movieId = :movieId AND s.date >= CURRENT_DATE " +
            "AND s.status = :status " +
            "ORDER BY s.date ASC")
    List<LocalDate> findDistinctFutureDatesByMovieId(@Param("movieId") Long movieId,
            @Param("status") ShowTimeStatus status);

    List<ShowTime> findByMovieIdAndDateAndStatusOrderByStartTimeAsc(Long movieId, LocalDate date,
            ShowTimeStatus status);

}