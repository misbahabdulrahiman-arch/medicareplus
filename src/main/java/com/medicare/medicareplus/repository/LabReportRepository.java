package com.medicare.medicareplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicare.medicareplus.model.LabReport;

public interface LabReportRepository
        extends JpaRepository<LabReport, Long> {

    List<LabReport> findByUserId(Long userId);

    List<LabReport> findByPatientName(String patientName);
}