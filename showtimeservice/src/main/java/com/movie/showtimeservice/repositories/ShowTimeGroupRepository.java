package com.movie.showtimeservice.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.movie.showtimeservice.dtos.responses.ShowTimeDetailRes;
import com.movie.showtimeservice.entities.ShowTimeGroup;
import com.movie.showtimeservice.enums.TypeShowTime;

public interface ShowTimeGroupRepository extends JpaRepository<ShowTimeGroup, Long> {
        List<ShowTimeGroup> findAllByRoomIdAndDate(Long roomId, LocalDate date);

        @Query("SELECT DISTINCT s.date FROM ShowTimeGroup s " +
                        "WHERE s.movieId = :movieId AND s.cinemaId = :cinemaId AND s.date >= CURRENT_DATE " +
                        "AND s.status = 'ACTIVE' " +
                        "ORDER BY s.date ASC")
        List<LocalDate> findDistinctFutureDatesByMovieIdAndCinemaId(@Param("movieId") Long movieId,
                        @Param("cinemaId") Long cinemaId);

        ShowTimeGroup findByMovieIdAndCinemaIdAndDateAndStatusAndType(Long movieId, Long cinemaId,
                        LocalDate date,
                        String status, TypeShowTime type);

        @Query("SELECT DISTINCT s.movieId FROM ShowTimeGroup s " +
                        "WHERE s.cinemaId = :cinemaId " +
                        "AND s.status = :status " +
                        "AND s.date >= CURRENT_DATE")
        List<Long> findNowShowingMovieIdsByCinemaId(@Param("cinemaId") Long cinemaId, String status);

        @Query("SELECT new com.movie.showtimeservice.dtos.responses.ShowTimeDetailRes(" +
                        "s.id, s.date, s.movieId, s.roomId, s.cinemaId, s.type, " +
                        "s.language, s.basePrice, s.duration, st.startTime, st.endTime) " +
                        "FROM ShowTimeGroup s JOIN s.showTimes st " +
                        "WHERE st.id = :id AND " +
                        "((s.date > :currentDate) OR (s.date = :currentDate AND st.startTime > :currentTime))")
        Optional<ShowTimeDetailRes> findBookableDetailShowTimeById(
                        @Param("id") Long id,
                        @Param("currentDate") LocalDate currentDate,
                        @Param("currentTime") LocalTime currentTime);

        @Query("SELECT s FROM ShowTimeGroup s " +
                        "WHERE s.status = 'ACTIVE' " +
                        "AND s.date = :date")
        List<ShowTimeGroup> findShowTimeGroupsInDate(@Param("date") LocalDate date);
}