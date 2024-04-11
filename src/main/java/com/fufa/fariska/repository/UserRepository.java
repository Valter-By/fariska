package com.fufa.fariska.repository;

import com.fufa.fariska.entity.GameUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<GameUser, Integer> {
    Optional<GameUser> findByName(String name);
}
