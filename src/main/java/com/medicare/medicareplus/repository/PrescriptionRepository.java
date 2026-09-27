package com.medicare.medicareplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicare.medicareplus.model.Prescription;

public interface PrescriptionRepository
        extends JpaRepository<Prescription, Long> {

    // Secure patient-specific search
    List<Prescription> findByUserId(Long userId);

    // Kept for compatibility with existing code
    List<Prescription> findByPatientName(String patientName);

}