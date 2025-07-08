package com.movie.movieservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.movie.movieservice.dtos.requests.MovieCreateDto;
import com.movie.movieservice.entities.Movie;

@Mapper
public interface MovieMapper {
    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);

    Movie toMovie(MovieCreateDto moviedto);
}
