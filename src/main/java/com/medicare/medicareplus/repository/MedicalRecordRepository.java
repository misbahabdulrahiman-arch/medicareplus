package com.medicare.medicareplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicare.medicareplus.model.MedicalRecord;

public interface MedicalRecordRepository
        extends JpaRepository<MedicalRecord, Long> {

    List<MedicalRecord> findByUserId(Long userId);

}