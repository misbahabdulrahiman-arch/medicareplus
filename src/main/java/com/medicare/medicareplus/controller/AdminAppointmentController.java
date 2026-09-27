package com.medicare.medicareplus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.medicare.medicareplus.model.Appointment;
import com.medicare.medicareplus.repository.AppointmentRepository;

@RestController
@RequestMapping("/api/admin/appointments")
public class AdminAppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;


    // =========================
    // GET ALL APPOINTMENTS
    // =========================

    @GetMapping
    public ResponseEntity<?> getAllAppointments() {

        List<Appointment> appointments =
                appointmentRepository.findAll();

        return ResponseEntity.ok(appointments);
    }


    // =========================
    // GET ONE APPOINTMENT
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointment(
            @PathVariable Long id) {

        Appointment appointment =
                appointmentRepository
                        .findById(id)
                        .orElse(null);

        if (appointment == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(appointment);
    }


    // =========================
    // DELETE APPOINTMENT
    // =========================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppointment(
            @PathVariable Long id) {

        if (!appointmentRepository.existsById(id)) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        appointmentRepository.deleteById(id);

        return ResponseEntity.ok(
                "Appointment deleted successfully."
        );
    }
}