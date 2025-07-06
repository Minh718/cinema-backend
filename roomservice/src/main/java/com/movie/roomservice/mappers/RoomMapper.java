package com.movie.roomservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.movie.roomservice.dtos.RoomCreateRequest;
import com.movie.roomservice.entities.Room;

@Mapper
public interface RoomMapper {
    RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);

    Room toRoom(RoomCreateRequest room);

}