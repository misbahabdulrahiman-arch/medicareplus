package com.medicare.medicareplus.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================================================
    // BASIC DOCTOR INFORMATION
    // =========================================================

    private String name;
    private String specialization;
    private String email;
    private String phone;
    private String password;
    private String qualification;

    private int experience;
    private double rating;
    private int totalPatients;
    private double consultationFee;

    // Existing availability field
    private String availability;

    private String profileImage;


    // =========================================================
    // REAL DOCTOR SCHEDULE
    // =========================================================

    /*
     * Example:
     *
     * workingDays = "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY"
     *
     * startTime = "09:00"
     *
     * endTime = "17:00"
     *
     * slotDuration = 30
     */

    private String workingDays;

    private String startTime;

    private String endTime;

    private int slotDuration;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public Doctor() {
    }


    // =========================================================
    // ID
    // =========================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    // =========================================================
    // NAME
    // =========================================================

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    // =========================================================
    // SPECIALIZATION
    // =========================================================

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }


    // =========================================================
    // EMAIL
    // =========================================================

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    // =========================================================
    // PHONE
    // =========================================================

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    // =========================================================
    // PASSWORD
    // =========================================================

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    // =========================================================
    // QUALIFICATION
    // =========================================================

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }


    // =========================================================
    // EXPERIENCE
    // =========================================================

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }


    // =========================================================
    // RATING
    // =========================================================

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }


    // =========================================================
    // TOTAL PATIENTS
    // =========================================================

    public int getTotalPatients() {
        return totalPatients;
    }

    public void setTotalPatients(int totalPatients) {
        this.totalPatients = totalPatients;
    }


    // =========================================================
    // CONSULTATION FEE
    // =========================================================

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }


    // =========================================================
    // AVAILABILITY
    // =========================================================

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }


    // =========================================================
    // PROFILE IMAGE
    // =========================================================

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }


    // =========================================================
    // WORKING DAYS
    // =========================================================

    public String getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(String workingDays) {
        this.workingDays = workingDays;
    }


    // =========================================================
    // START TIME
    // =========================================================

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }


    // =========================================================
    // END TIME
    // =========================================================

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    // =========================================================
    // SLOT DURATION
    // =========================================================

    public int getSlotDuration() {
        return slotDuration;
    }

    public void setSlotDuration(int slotDuration) {
        this.slotDuration = slotDuration;
    }
}