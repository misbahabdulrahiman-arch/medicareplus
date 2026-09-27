package com.medicare.medicareplus.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicare.medicareplus.model.Doctor;

public interface DoctorRepository
        extends JpaRepository<Doctor, Long> {

    List<Doctor> findBySpecializationIgnoreCase(
            String specialization
    );

    List<Doctor> findByNameContainingIgnoreCase(
            String name
    );

    Optional<Doctor> findByEmail(
            String email
    );
}