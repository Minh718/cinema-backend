package com.movie.bookingservice.repositories.httpClients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.movie.bookingservice.dtos.responses.ApiRes;
import com.movie.bookingservice.dtos.responses.SeatResponse;

@FeignClient(name = "room-service")
public interface RoomClient {

    @GetMapping("/{roomId}/seats")
    ApiRes<List<SeatResponse>> getSeatsByRoomId(@PathVariable("roomId") Long roomId);
}
