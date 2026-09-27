package com.medicare.medicareplus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDoctorManagementController {

    @GetMapping("/admin-doctors")
    public String doctors() {
        return "admin-doctors";
    }
}