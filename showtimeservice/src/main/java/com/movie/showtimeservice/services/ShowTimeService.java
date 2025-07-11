package com.movie.showtimeservice.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.movie.showtimeservice.enums.ShowTimeStatus;
import org.springframework.stereotype.Service;

import com.movie.showtimeservice.dtos.requests.AutoAssignRequest;
import com.movie.showtimeservice.dtos.responses.ApiRes;
import com.movie.showtimeservice.dtos.responses.SeatResponse;
import com.movie.showtimeservice.entities.ShowTime;
import com.movie.showtimeservice.entities.ShowTimeSeat;
import com.movie.showtimeservice.repositories.ShowTimeRepository;
import com.movie.showtimeservice.repositories.ShowTimeSeatRepository;
import com.movie.showtimeservice.repositories.httpClient.RoomClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShowTimeService {

    private final ShowTimeRepository showTimeRepository;
    private final ShowTimeSeatRepository showTimeSeatRepository;
    private final RoomClient roomClient;

    public ShowTime autoAssign(AutoAssignRequest request) {
        int bufferMinutes = 15;
        LocalTime opening = LocalTime.of(8, 0);
        LocalTime closing = LocalTime.of(23, 0);

        List<ShowTime> existing = showTimeRepository.findByRoomIdAndDateOrderByStartTimeAsc(
                request.getRoomId(), request.getDate());

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
                .subtitle("VN")
                .basePrice(5.5)
                .status(ShowTimeStatus.SCHEDULED)
                .build();
        ShowTime savedShowTime = showTimeRepository.save(showTime);
        ApiRes<List<SeatResponse>> res = roomClient.getSeatsByRoomId(request.getRoomId());
        List<SeatResponse> seats = res.getResult();
        List<ShowTimeSeat> showTimeSeats = new ArrayList<>();
        if (seats != null) {
            for (SeatResponse seat : seats) {
                showTimeSeats.add(ShowTimeSeat.builder()
                        .seatId(seat.getId())
                        .seatCode(seat.getCode())
                        .available(true)
                        .showTime(savedShowTime)
                        .build());
            }
            showTimeSeatRepository.saveAll(showTimeSeats);
        }

        return showTimeRepository.save(showTime);
    }

    public List<LocalDate> getFutureShowDatesByMovieId(Long movieId) {
        return showTimeRepository.findDistinctFutureDatesByMovieId(movieId, ShowTimeStatus.SCHEDULED);
    }
    public List<ShowTime> getShowTimesByMovieIdAndDate(Long movieId, LocalDate date) {
        return showTimeRepository.findByMovieIdAndDateAndStatusOrderByStartTimeAsc(movieId, date, ShowTimeStatus.SCHEDULED);
    }
}