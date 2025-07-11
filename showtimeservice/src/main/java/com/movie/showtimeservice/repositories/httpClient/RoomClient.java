package com.movie.showtimeservice.repositories.httpClient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.movie.showtimeservice.dtos.responses.ApiRes;
import com.movie.showtimeservice.dtos.responses.SeatResponse;

@FeignClient(name = "room-service")
public interface RoomClient {

    @GetMapping("/{roomId}/seats")
    ApiRes<List<SeatResponse>> getSeatsByRoomId(@PathVariable("roomId") Long roomId);
}