package com.movie.showtimeservice.repositories.httpClient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.movie.showtimeservice.dtos.responses.ApiRes;
import com.movie.showtimeservice.dtos.responses.SeatResponse;

@FeignClient(name = "cinema-service")
public interface CinemaClient {

    @GetMapping("/{roomId}/seats")
    ApiRes<List<SeatResponse>> getSeatsByRoomId(@PathVariable("roomId") Long roomId);
}