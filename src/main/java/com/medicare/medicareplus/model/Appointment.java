package com.medicare.medicareplus.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================================================
    // DOCTOR INFORMATION
    // =========================================================

    private Long doctorId;
    private String doctorName;


    // =========================================================
    // PATIENT INFORMATION
    // =========================================================

    // REAL patient database ID
    private Long patientId;

    // Kept for display purposes
    private String patientName;


    // =========================================================
    // APPOINTMENT DETAILS
    // =========================================================

    private String appointmentDate;
    private String appointmentTime;
    private String reason;
    private String consultationType;


    // =========================================================
    // STATUS & PAYMENT
    // =========================================================

    private String status;
    private double fee;


    // =========================================================
    // DEFAULT CONSTRUCTOR
    // =========================================================

    public Appointment() {
    }


    // =========================================================
    // APPOINTMENT ID
    // =========================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    // =========================================================
    // DOCTOR ID
    // =========================================================

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }


    // =========================================================
    // DOCTOR NAME
    // =========================================================

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }


    // =========================================================
    // PATIENT ID
    // =========================================================

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }


    // =========================================================
    // PATIENT NAME
    // =========================================================

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }


    // =========================================================
    // APPOINTMENT DATE
    // =========================================================

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }


    // =========================================================
    // APPOINTMENT TIME
    // =========================================================

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }


    // =========================================================
    // REASON
    // =========================================================

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


    // =========================================================
    // CONSULTATION TYPE
    // =========================================================

    public String getConsultationType() {
        return consultationType;
    }

    public void setConsultationType(String consultationType) {
        this.consultationType = consultationType;
    }


    // =========================================================
    // STATUS
    // =========================================================

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    // =========================================================
    // FEE
    // =========================================================

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }
}