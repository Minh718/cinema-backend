package com.movie.cinemaservice.controllers;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.movie.cinemaservice.dtos.requests.CinemaCreateReq;
import com.movie.cinemaservice.dtos.requests.RoomCreateReq;
import com.movie.cinemaservice.dtos.responses.ApiRes;
import com.movie.cinemaservice.dtos.responses.CinemaRes;
import com.movie.cinemaservice.dtos.responses.RoomDetailRes;
import com.movie.cinemaservice.dtos.responses.SeatResponse;
import com.movie.cinemaservice.entities.Cinema;
import com.movie.cinemaservice.entities.Room;
import com.movie.cinemaservice.services.CinemaService;
import com.movie.cinemaservice.services.RoomService;

import jakarta.ws.rs.HeaderParam;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cinemas")
@RequiredArgsConstructor
public class CinemaController {

    private final RoomService roomService;
    private final CinemaService cinemaService;

    @PostMapping("/manager/create-room")
    public ApiRes<Room> createRoom(@RequestBody RoomCreateReq room, @HeaderParam("X-User-Id") String userId) {
        return ApiRes.<Room>builder().code(200).message("Success").result(roomService.createRoom(room, userId)).build();
    }

    @CacheEvict(value = "getAllCinemas", allEntries = true)
    @PostMapping(path = "/admin/create-cinema", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiRes<Cinema> createCinema(@RequestPart("data") CinemaCreateReq request,
            @RequestPart(value = "image", required = true) MultipartFile image) {
        Cinema cinema = cinemaService.createCinema(request, image);
        return ApiRes.<Cinema>builder().result(cinema).code(1000).message("Successfully").build();
    }

    @Cacheable(value = "getAllCinemas")
    @GetMapping("/public")
    public ApiRes<List<CinemaRes>> getAllCinemas() {
        List<CinemaRes> seats = cinemaService.getAllCinemas();
        return ApiRes.<List<CinemaRes>>builder().code(200).message("Success").result(seats).build();
    }

    @Cacheable(value = "getDetailRoom", key = "#roomId")
    @GetMapping("/{roomId}/room")
    public ApiRes<RoomDetailRes> getDetailRoom(@PathVariable Long roomId) {
        RoomDetailRes room = roomService.getRoomDetail(roomId);
        return ApiRes.<RoomDetailRes>builder().code(200).message("Success").result(room).build();
    }
}
