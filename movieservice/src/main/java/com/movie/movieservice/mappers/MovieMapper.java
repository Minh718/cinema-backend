package com.movie.movieservice.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.movie.movieservice.dtos.requests.MovieRequestDto;
import com.movie.movieservice.dtos.responses.MovieDetailRes;
import com.movie.movieservice.dtos.responses.MovieRes;
import com.movie.movieservice.dtos.responses.MovieNameRes;
import com.movie.movieservice.entities.Movie;

@Mapper
public interface MovieMapper {
    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);

    Movie toMovie(MovieRequestDto moviedto);

    MovieDetailRes toMovieDetailRes(Movie movie);

    MovieNameRes toMovieNameRes(Movie movie);

    MovieRes toMovieRes(Movie movie);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMovieFromDto(MovieRequestDto dto, @MappingTarget Movie movie);
}
