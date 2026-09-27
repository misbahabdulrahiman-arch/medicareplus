package com.medicare.medicareplus.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.medicare.medicareplus.model.Appointment;
import com.medicare.medicareplus.model.MedicalRecord;
import com.medicare.medicareplus.model.Notification;
import com.medicare.medicareplus.model.Prescription;

import com.medicare.medicareplus.repository.AppointmentRepository;
import com.medicare.medicareplus.repository.MedicalRecordRepository;
import com.medicare.medicareplus.repository.NotificationRepository;
import com.medicare.medicareplus.repository.PrescriptionRepository;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/consultations")
public class ConsultationController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private NotificationRepository notificationRepository;


    // =========================================================
    // COMPLETE CONSULTATION
    // =========================================================

    @PostMapping("/{appointmentId}/complete")
    public ResponseEntity<?> completeConsultation(
            @PathVariable Long appointmentId,
            @RequestBody ConsultationRequest request,
            HttpSession session) {

        // =====================================================
        // CHECK DOCTOR LOGIN
        // =====================================================

        Object doctorIdObject =
                session.getAttribute("doctorId");

        if (doctorIdObject == null) {

            return ResponseEntity
                    .status(401)
                    .body("Please login as a doctor first.");
        }

        Long doctorId =
                Long.valueOf(
                        doctorIdObject.toString()
                );


        // =====================================================
        // FIND APPOINTMENT
        // =====================================================

        Appointment appointment =
                appointmentRepository
                        .findById(appointmentId)
                        .orElse(null);

        if (appointment == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }


        // =====================================================
        // VERIFY APPOINTMENT BELONGS TO DOCTOR
        // =====================================================

        if (appointment.getDoctorId() == null
                || !appointment.getDoctorId().equals(doctorId)) {

            return ResponseEntity
                    .status(403)
                    .body(
                        "You cannot complete this appointment."
                    );
        }


        // =====================================================
        // VERIFY PATIENT ID
        // =====================================================

        if (appointment.getPatientId() == null) {

            return ResponseEntity
                    .badRequest()
                    .body(
                        "This appointment is not linked to a patient account."
                    );
        }


        // =====================================================
        // VALIDATE CONSULTATION
        // =====================================================

        if (request.diagnosis == null
                || request.diagnosis.isBlank()) {

            return ResponseEntity
                    .badRequest()
                    .body(
                        "Diagnosis or consultation summary is required."
                    );
        }


        // =====================================================
        // GET CURRENT DATE
        // =====================================================

        String today =
                LocalDate.now().toString();


        // =====================================================
        // CREATE MEDICAL RECORD
        // =====================================================

        MedicalRecord record =
                new MedicalRecord();

        record.setUserId(
                appointment.getPatientId()
        );

        record.setPatientName(
                appointment.getPatientName()
        );

        record.setDoctorName(
                appointment.getDoctorName()
        );

        record.setDiagnosis(
                request.diagnosis
        );

        record.setRecordDate(
                today
        );

        record.setNotes(
                request.notes
        );


        MedicalRecord savedRecord =
                medicalRecordRepository.save(record);


        // =====================================================
        // CREATE PRESCRIPTION
        // =====================================================

        Prescription savedPrescription = null;


        /*
         * Prescription is created only if the doctor
         * actually entered a medicine.
         */

        if (request.medicine != null
                && !request.medicine.isBlank()) {

            Prescription prescription =
                    new Prescription();

            prescription.setUserId(
                    appointment.getPatientId()
            );

            prescription.setPatientName(
                    appointment.getPatientName()
            );

            prescription.setDoctorName(
                    appointment.getDoctorName()
            );

            prescription.setMedicine(
                    request.medicine
            );

            prescription.setMorning(
                    request.morning
            );

            prescription.setAfternoon(
                    request.afternoon
            );

            prescription.setNight(
                    request.night
            );

            prescription.setDuration(
                    request.duration
            );

            prescription.setInstructions(
                    request.instructions
            );

            prescription.setPrescriptionDate(
                    today
            );


            savedPrescription =
                    prescriptionRepository.save(
                            prescription
                    );
        }


        // =====================================================
        // MARK APPOINTMENT AS COMPLETED
        // =====================================================

        appointment.setStatus(
                "COMPLETED"
        );

        appointmentRepository.save(
                appointment
        );


        // =====================================================
        // PATIENT NOTIFICATION
        // =====================================================

        Notification notification =
                new Notification();

        notification.setUserId(
                appointment.getPatientId()
        );

        notification.setTitle(
                "Consultation Completed"
        );

        String message =
                "Your consultation with "
                + appointment.getDoctorName()
                + " has been completed. "
                + "Your medical record is now available.";

        if (savedPrescription != null) {

            message +=
                    " A new prescription has also been added.";
        }

        notification.setMessage(
                message
        );

        notification.setType(
                "CONSULTATION"
        );

        notification.setNotificationDate(
                today
        );

        notification.setReadStatus(
                false
        );

        notificationRepository.save(
                notification
        );


        // =====================================================
        // RESPONSE
        // =====================================================

        ConsultationResponse response =
                new ConsultationResponse();

        response.appointment =
                appointment;

        response.medicalRecord =
                savedRecord;

        response.prescription =
                savedPrescription;

        response.message =
                "Consultation completed successfully.";


        return ResponseEntity.ok(
                response
        );
    }


    // =========================================================
    // REQUEST CLASS
    // =========================================================

    public static class ConsultationRequest {

        public String diagnosis;

        public String notes;

        public String medicine;

        public String morning;

        public String afternoon;

        public String night;

        public String duration;

        public String instructions;
    }


    // =========================================================
    // RESPONSE CLASS
    // =========================================================

    public static class ConsultationResponse {

        public String message;

        public Appointment appointment;

        public MedicalRecord medicalRecord;

        public Prescription prescription;
    }
}