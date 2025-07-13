package com.movie.cinemaservice.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.movie.cinemaservice.dtos.requests.CinemaCreateReq;
import com.movie.cinemaservice.dtos.responses.CinemaRes;
import com.movie.cinemaservice.entities.Cinema;

@Mapper
public interface CinemaMapper {
    CinemaMapper INSTANCE = Mappers.getMapper(CinemaMapper.class);

    Cinema toCinema(CinemaCreateReq cinema);

    CinemaRes toCinemaRes(Cinema cinema);

    List<CinemaRes> toCinemaResList(List<Cinema> cinemas);

}