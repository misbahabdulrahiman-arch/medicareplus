package com.medicare.medicareplus.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medicare.medicareplus.repository.AppointmentRepository;
import com.medicare.medicareplus.repository.DoctorRepository;
import com.medicare.medicareplus.repository.UserRepository;

@RestController
@RequestMapping("/api/admin")
public class AdminStatsController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;


    @GetMapping("/stats")
    public Map<String, Object> getStats() {

        Map<String, Object> stats =
                new HashMap<>();


        // =========================
        // TOTAL PATIENTS
        // =========================

        long totalPatients =
                userRepository.countByRoleIgnoreCase(
                        "PATIENT"
                );


        // =========================
        // TOTAL DOCTORS
        // =========================

        long totalDoctors =
                doctorRepository.count();


        // =========================
        // TOTAL APPOINTMENTS
        // =========================

        long totalAppointments =
                appointmentRepository.count();


        // =========================
        // COMPLETED APPOINTMENTS
        // =========================

        long completedAppointments =
                appointmentRepository.findAll()
                        .stream()
                        .filter(a ->
                                "COMPLETED".equalsIgnoreCase(
                                        a.getStatus()
                                )
                        )
                        .count();


        // =========================
        // SEND STATISTICS
        // =========================

        stats.put(
                "patients",
                totalPatients
        );

        stats.put(
                "doctors",
                totalDoctors
        );

        stats.put(
                "appointments",
                totalAppointments
        );

        stats.put(
                "completed",
                completedAppointments
        );


        return stats;
    }
}