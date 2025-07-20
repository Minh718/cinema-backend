package com.movie.cinemaservice.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie.cinemaservice.dtos.responses.ApiRes;
import com.movie.cinemaservice.dtos.responses.CinemaRes;
import com.movie.cinemaservice.dtos.responses.SeatResponse;
import com.movie.cinemaservice.services.CinemaService;
import com.movie.cinemaservice.services.RoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("internal/cinemas")
@RequiredArgsConstructor
public class InternalCinemaControler {
    private final RoomService roomService;
    private final CinemaService cinemaService;

    @GetMapping("/{roomId}/seats")
    public ApiRes<List<SeatResponse>> getSeatsByRoomId(@PathVariable Long roomId) {
        List<SeatResponse> seats = roomService.getSeatsByRoomId(roomId);
        return ApiRes.<List<SeatResponse>>builder().code(200).message("Success").result(seats).build();
    }

    @GetMapping("/{cinemaId}")
    public ApiRes<CinemaRes> getDetailCinema(@PathVariable Long cinemaId) {
        CinemaRes cinema = cinemaService.getDetailCinema(cinemaId);
        return ApiRes.<CinemaRes>builder().code(200).message("Success").result(cinema).build();
    }
}
