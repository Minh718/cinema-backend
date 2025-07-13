package com.movie.cinemaservice.repositories;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.movie.cinemaservice.entities.Cinema;
import com.movie.cinemaservice.entities.Room;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Long> {
    Optional<Cinema> findByManagerId(String managerId);

    List<Cinema> findAllByStatus(boolean status);
}