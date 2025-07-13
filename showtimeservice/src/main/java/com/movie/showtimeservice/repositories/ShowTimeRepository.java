package com.movie.showtimeservice.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie.showtimeservice.entities.ShowTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.movie.showtimeservice.enums.ShowTimeStatus;

public interface ShowTimeRepository extends JpaRepository<ShowTime, Long> {

        List<ShowTime> findByRoomIdAndCinemaIdIdAndDateOrderByStartTimeAsc(Long roomId, Long cinemaId, LocalDate date);

        @Query("SELECT DISTINCT s.date FROM ShowTime s " +
                        "WHERE s.movieId = :movieId AND s.cinemaId = :cinemaId AND s.date >= CURRENT_DATE " +
                        "AND s.status = :status " +
                        "ORDER BY s.date ASC")
        List<LocalDate> findDistinctFutureDatesByMovieIdAndCinemaId(@Param("movieId") Long movieId,
                        @Param("cinemaId") Long cinemaId,
                        @Param("status") ShowTimeStatus status);

        List<ShowTime> findByMovieIdAndCinemaIdAndDateAndStatusOrderByStartTimeAsc(Long movieId, Long cinemaId,
                        LocalDate date,
                        ShowTimeStatus status);

        @Query("SELECT DISTINCT s.movieId FROM ShowTime s " +
                        "WHERE s.cinemaId = :cinemaId " +
                        "AND s.status = :status " +
                        "AND s.date >= CURRENT_DATE")
        List<Long> findNowShowingMovieIdsByCinemaId(@Param("cinemaId") Long cinemaId, ShowTimeStatus status);
}