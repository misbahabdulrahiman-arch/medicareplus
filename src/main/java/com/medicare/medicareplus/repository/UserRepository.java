package com.medicare.medicareplus.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicare.medicareplus.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);

    Optional<User> findTopByEmailAndRoleIgnoreCase(
            String email,
            String role
    );

    long countByRoleIgnoreCase(String role);

    List<User> findByRoleIgnoreCase(String role);
}