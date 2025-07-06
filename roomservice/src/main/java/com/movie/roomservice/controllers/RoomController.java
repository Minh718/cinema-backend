package com.movie.roomservice.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie.roomservice.dtos.RoomCreateRequest;
import com.movie.roomservice.entities.Room;
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

    // @GetMapping
    // public List<Room> getRooms() {
    // return roomService.getAllRooms();
    // }
}
