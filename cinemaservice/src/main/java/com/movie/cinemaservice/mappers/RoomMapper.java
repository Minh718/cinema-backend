package com.movie.cinemaservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.movie.cinemaservice.dtos.requests.RoomCreateReq;
import com.movie.cinemaservice.dtos.responses.RoomDetailRes;
import com.movie.cinemaservice.entities.Room;

@Mapper
public interface RoomMapper {
    RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);

    Room toRoom(RoomCreateReq room);

    RoomDetailRes toRoomDetailRes(Room room);
}