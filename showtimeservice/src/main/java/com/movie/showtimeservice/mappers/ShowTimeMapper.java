package com.movie.showtimeservice.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.movie.showtimeservice.dtos.responses.ShowTimeRes;
import com.movie.showtimeservice.entities.ShowTime;

@Mapper
public interface ShowTimeMapper {
    ShowTimeMapper INSTANCE = Mappers.getMapper(ShowTimeMapper.class);

    ShowTimeRes toShowTimeRes(ShowTime showTime); // ShowTimeRes

    List<ShowTimeRes> toShowTimeRes(List<ShowTime> showTimes);
}
