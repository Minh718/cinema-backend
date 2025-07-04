package com.movie.authservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.movie.authservice.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}