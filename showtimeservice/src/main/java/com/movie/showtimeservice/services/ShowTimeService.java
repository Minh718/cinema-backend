package com.movie.showtimeservice.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.movie.showtimeservice.enums.ShowTimeStatus;
import com.movie.showtimeservice.mappers.ShowTimeMapper;

import org.springframework.stereotype.Service;

import com.movie.showtimeservice.dtos.requests.AutoAssignRequest;
import com.movie.showtimeservice.dtos.responses.ApiRes;
import com.movie.showtimeservice.dtos.responses.SeatResponse;
import com.movie.showtimeservice.dtos.responses.ShowTimeRes;
import com.movie.showtimeservice.entities.ShowTime;
import com.movie.showtimeservice.repositories.ShowTimeRepository;
import com.movie.showtimeservice.repositories.httpClient.CinemaClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShowTimeService {

    private final ShowTimeRepository showTimeRepository;
    private final CinemaClient cinemaClient;

    public ShowTime autoAssign(AutoAssignRequest request) {
        int bufferMinutes = 15;
        LocalTime opening = LocalTime.of(8, 0);
        LocalTime closing = LocalTime.of(23, 0);

        List<ShowTime> existing = showTimeRepository.findByRoomIdAndCinemaIdIdAndDateOrderByStartTimeAsc(
                request.getRoomId(), request.getCinemaId(), request.getDate());

        LocalTime currentStart = opening;

        for (ShowTime show : existing) {
            LocalTime currentEnd = currentStart.plusMinutes(request.getMovieDuration() + bufferMinutes);
            if (currentEnd.isBefore(show.getStartTime())) {
                return saveShowTime(currentStart, request, request.getMovieDuration());
            }
            currentStart = show.getEndTime().plusMinutes(bufferMinutes);
        }

        if (currentStart.plusMinutes(request.getMovieDuration()).isBefore(closing)) {
            return saveShowTime(currentStart, request, request.getMovieDuration());
        }

        throw new RuntimeException("No available time slot");
    }

    private ShowTime saveShowTime(LocalTime start, AutoAssignRequest request, int duration) {
        LocalTime end = start.plusMinutes(duration);
        ShowTime showTime = ShowTime.builder()
                .date(request.getDate())
                .startTime(start)
                .endTime(end)
                .movieId(request.getMovieId())
                .roomId(request.getRoomId())
                .screenType("2D")
                .language("EN")
                .cinemaId(request.getCinemaId())
                .subtitle("VN")
                .basePrice(5.5)
                .status(ShowTimeStatus.SCHEDULED)
                .build();
        ShowTime savedShowTime = showTimeRepository.save(showTime);
        return showTimeRepository.save(showTime);
    }

    public List<LocalDate> getFutureShowDatesByMovieIdAndCinemaId(Long movieId, Long cinemaId) {
        return showTimeRepository.findDistinctFutureDatesByMovieIdAndCinemaId(movieId, cinemaId,
                ShowTimeStatus.SCHEDULED);
    }

    public List<ShowTime> getShowTimesByMovieIdAndCinemaIdAndDate(Long movieId, Long cinemaId, LocalDate date) {
        return showTimeRepository.findByMovieIdAndCinemaIdAndDateAndStatusOrderByStartTimeAsc(movieId, cinemaId, date,
                ShowTimeStatus.SCHEDULED);
    }

    public List<Long> getNowShowingMovieIdsByCinemaId(Long cinemaId) {

        return showTimeRepository.findNowShowingMovieIdsByCinemaId(cinemaId, ShowTimeStatus.SCHEDULED);
    }

    public ShowTimeRes getBookableShowTime(Long showTimeId) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        ShowTime showtime = showTimeRepository.findBookableShowTimeById(showTimeId, today, now)
                .orElseThrow(() -> new IllegalArgumentException("Showtime is not available for booking."));
        return ShowTimeMapper.INSTANCE.toShowTimeRes(showtime);
    }
}