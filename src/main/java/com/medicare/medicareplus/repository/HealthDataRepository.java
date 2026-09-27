package com.medicare.medicareplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicare.medicareplus.model.HealthData;

public interface HealthDataRepository
        extends JpaRepository<HealthData, Long> {

    List<HealthData> findByUserId(Long userId);
}