package com.movie.authservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.movie.authservice.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

}