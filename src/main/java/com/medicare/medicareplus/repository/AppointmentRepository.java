package com.medicare.medicareplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicare.medicareplus.model.Appointment;

public interface AppointmentRepository
        extends JpaRepository<Appointment, Long> {

    // =========================================================
    // PATIENT APPOINTMENTS
    // =========================================================

    List<Appointment> findByPatientId(Long patientId);


    // =========================================================
    // OLD NAME-BASED SEARCH
    // Kept so existing data/features don't break
    // =========================================================

    List<Appointment> findByPatientName(String patientName);


    // =========================================================
    // DOCTOR APPOINTMENTS
    // =========================================================

    List<Appointment> findByDoctorId(Long doctorId);


    // =========================================================
    // DOUBLE-BOOKING CHECK
    // =========================================================

    boolean existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndStatusNot(
            Long doctorId,
            String appointmentDate,
            String appointmentTime,
            String status
    );
}