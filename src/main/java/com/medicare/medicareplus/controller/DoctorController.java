package com.medicare.medicareplus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.medicare.medicareplus.model.Doctor;
import com.medicare.medicareplus.repository.DoctorRepository;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorRepository doctorRepository;

    // GET ALL DOCTORS
    @GetMapping
    public ResponseEntity<?> getAllDoctors() {

        List<Doctor> doctors =
                doctorRepository.findAll();

        return ResponseEntity.ok(doctors);
    }

    // GET DOCTOR BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctorById(
            @PathVariable Long id) {

        Doctor doctor =
                doctorRepository
                        .findById(id)
                        .orElse(null);

        if (doctor == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(doctor);
    }

    // SEARCH DOCTORS BY NAME
    @GetMapping("/search")
    public ResponseEntity<?> searchDoctors(
            @RequestParam String name) {

        List<Doctor> doctors =
                doctorRepository
                        .findByNameContainingIgnoreCase(name);

        return ResponseEntity.ok(doctors);
    }

    // FILTER BY SPECIALIZATION
    @GetMapping("/specialization")
    public ResponseEntity<?> getBySpecialization(
            @RequestParam String name) {

        List<Doctor> doctors =
                doctorRepository
                        .findBySpecializationIgnoreCase(name);

        return ResponseEntity.ok(doctors);
    }

    // ADD DOCTOR
    @PostMapping
    public ResponseEntity<?> addDoctor(
            @RequestBody Doctor doctor) {

        Doctor savedDoctor =
                doctorRepository.save(doctor);

        return ResponseEntity.ok(savedDoctor);
    }

    // UPDATE DOCTOR
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDoctor(
            @PathVariable Long id,
            @RequestBody Doctor updatedDoctor) {

        Doctor doctor =
                doctorRepository
                        .findById(id)
                        .orElse(null);

        if (doctor == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        doctor.setName(
                updatedDoctor.getName()
        );

        doctor.setSpecialization(
                updatedDoctor.getSpecialization()
        );

        doctor.setEmail(
                updatedDoctor.getEmail()
        );

        doctor.setPhone(
                updatedDoctor.getPhone()
        );

        doctor.setQualification(
                updatedDoctor.getQualification()
        );

        doctor.setExperience(
                updatedDoctor.getExperience()
        );

        doctor.setRating(
                updatedDoctor.getRating()
        );

        doctor.setTotalPatients(
                updatedDoctor.getTotalPatients()
        );

        doctor.setConsultationFee(
                updatedDoctor.getConsultationFee()
        );

        doctor.setAvailability(
                updatedDoctor.getAvailability()
        );

        doctor.setProfileImage(
                updatedDoctor.getProfileImage()
        );

        Doctor savedDoctor =
                doctorRepository.save(doctor);

        return ResponseEntity.ok(savedDoctor);
    }

    // DELETE DOCTOR
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDoctor(
            @PathVariable Long id) {

        if (!doctorRepository.existsById(id)) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        doctorRepository.deleteById(id);

        return ResponseEntity.ok(
                "Doctor deleted successfully."
        );
    }
}