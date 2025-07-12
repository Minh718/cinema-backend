package com.movie.roomservice.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie.roomservice.dtos.RoomCreateRequest;
import com.movie.roomservice.dtos.responses.ApiRes;
import com.movie.roomservice.dtos.responses.SeatResponse;
import com.movie.roomservice.entities.Room;
import com.movie.roomservice.entities.Seat;
import com.movie.roomservice.services.RoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public Room createRoom(@RequestBody RoomCreateRequest room) {
        return roomService.createRoom(room);
    }

    @GetMapping("/{roomId}/seats")
    public ApiRes<List<SeatResponse>> getSeatsByRoomId(@PathVariable Long roomId) {
        List<SeatResponse> seats = roomService.getSeatsByRoomId(roomId);
        return ApiRes.<List<SeatResponse>>builder().code(200).message("Success").result(seats).build();
    }
    // @GetMapping
    // public List<Room> getRooms() {
    // return roomService.getAllRooms();
    // }
}
