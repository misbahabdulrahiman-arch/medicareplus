package com.medicare.medicareplus.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    @PostMapping
    public Map<String, String> chat(
            @RequestBody Map<String, String> request) {

        String message = request.get("message");

        if (message == null) {
            message = "";
        }

        message = message.toLowerCase().trim();

        String reply;


        // APPOINTMENT
        if (message.contains("book")
                || message.contains("appointment")) {

            reply = "Sure! 💙 You can book an appointment by choosing a doctor, selecting a date and time, and confirming your consultation.";
        }


        // DOCTOR
        else if (message.contains("doctor")
                || message.contains("specialist")) {

            reply = "You can find doctors by specialty from the Find a Doctor section. I can help you navigate there! 👨‍⚕️";
        }


        // APPOINTMENTS
        else if (message.contains("my appointment")
                || message.contains("appointments")) {

            reply = "You can view, cancel or reschedule your appointments from your Appointments page. 📅";
        }


        // HEALTH
        else if (message.contains("health")
                || message.contains("heart")
                || message.contains("oxygen")
                || message.contains("sleep")) {

            reply = "Your latest health information is available in Health Overview. You can view heart rate, blood pressure, oxygen level, sleep and weight there. 💙";
        }


        // RECORDS
        else if (message.contains("record")
                || message.contains("medical")) {

            reply = "Your medical records are available in the Medical Records section. 🗂️";
        }


        // PRESCRIPTION
        else if (message.contains("prescription")
                || message.contains("medicine")) {

            reply = "You can view your prescriptions and medication instructions from the Prescriptions section. 💊";
        }


        // LAB
        else if (message.contains("lab")
                || message.contains("report")
                || message.contains("test")) {

            reply = "Your available laboratory reports can be viewed in the Lab Reports section. 🧪";
        }


        // EMERGENCY
        else if (message.contains("emergency")
                || message.contains("urgent")) {

            reply = "If this is a medical emergency, please contact your local emergency service or go to the nearest emergency department immediately. 🚨";
        }


        // HELLO
        else if (message.contains("hello")
                || message.contains("hi")
                || message.contains("hey")) {

            reply = "Hey! 👋 I'm MediBot. I can help you navigate appointments, doctors, health information, prescriptions and medical records.";
        }


        // THANK YOU
        else if (message.contains("thank")) {

            reply = "You're very welcome! 💙 I'm always here to help you navigate MediCare+.";
        }


        // DEFAULT
        else {

            reply = "I'm still learning! 🤖 Try asking me about appointments, doctors, health, prescriptions, medical records or lab reports.";
        }


        Map<String, String> response =
                new HashMap<>();

        response.put("reply", reply);

        return response;
    }
}