package com.movie.cinemaservice.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.movie.cinemaservice.dtos.requests.RoomCreateReq;
import com.movie.cinemaservice.dtos.responses.SeatResponse;
import com.movie.cinemaservice.entities.Cinema;
import com.movie.cinemaservice.entities.Room;
import com.movie.cinemaservice.entities.Seat;
import com.movie.cinemaservice.exceptions.CustomException;
import com.movie.cinemaservice.exceptions.ErrorCode;
import com.movie.cinemaservice.mappers.RoomMapper;
import com.movie.cinemaservice.repositories.CinemaRepository;
import com.movie.cinemaservice.repositories.RoomRepository;
import com.movie.cinemaservice.repositories.SeatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final SeatRepository seatRepository;
    private final CinemaRepository cinemaRepository;

    public Room createRoom(RoomCreateReq roomdto, String userId) {
        Room room = RoomMapper.INSTANCE.toRoom(roomdto);
        Cinema cinema = cinemaRepository.findByManagerId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.CINEMA_NOT_FOUND));
        room.setCinema(cinema);
        Room savedRoom = roomRepository.save(room);

        List<Seat> seats = new ArrayList<>();

        for (int row = 0; row < room.getNumberOfRows(); row++) {
            char rowLetter = (char) ('A' + row);

            for (int seatNum = 1; seatNum <= room.getSeatsPerRow(); seatNum++) {
                String code = rowLetter + String.valueOf(seatNum);
                Seat seat = Seat.builder()
                        .code(code)
                        .room(savedRoom)
                        .build();
                seats.add(seat);
            }
        }

        // Save all seats
        seatRepository.saveAll(seats);
        savedRoom.setSeats(seats);

        return savedRoom;
    }

    public List<SeatResponse> getSeatsByRoomId(Long roomId) {
        List<Seat> seats = seatRepository.findByRoomId(roomId);
        return seats.stream()
                .map(seat -> new SeatResponse(seat.getId(), seat.getCode()))
                .toList();
    }
}
