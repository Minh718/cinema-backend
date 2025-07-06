package com.movie.roomservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.movie.roomservice.entities.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}