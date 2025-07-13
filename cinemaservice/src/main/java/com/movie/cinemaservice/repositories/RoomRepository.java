package com.movie.cinemaservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.movie.cinemaservice.entities.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}