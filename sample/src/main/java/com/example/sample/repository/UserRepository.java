package com.example.sample.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sample.model.User;

public interface UserRepository extends JpaRepository<User,UUID> {
    
    public  boolean existsByEmail(String email);
    public Optional<User> findByEmail(String email);
}
