package com.medicare.medicareplus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.medicare.medicareplus.model.Doctor;
import com.medicare.medicareplus.repository.DoctorRepository;

@RestController
@RequestMapping("/api/admin/doctors")
public class AdminDoctorController {

    @Autowired
    private DoctorRepository doctorRepository;

    @GetMapping
    public ResponseEntity<?> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctor(@PathVariable Long id) {

        Doctor doctor = doctorRepository.findById(id).orElse(null);

        if (doctor == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(doctor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Long id) {

        if (!doctorRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        doctorRepository.deleteById(id);

        return ResponseEntity.ok("Doctor deleted successfully.");
    }
}