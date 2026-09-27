package com.medicare.medicareplus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.medicare.medicareplus.model.Notification;
import com.medicare.medicareplus.repository.NotificationRepository;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    // Get notifications for logged-in user
    @GetMapping
    public ResponseEntity<?> getNotifications(
            HttpSession session) {

        Object userIdObject =
                session.getAttribute("userId");

        if (userIdObject == null) {
            return ResponseEntity
                    .status(401)
                    .body("Please login first.");
        }

        Long userId =
                Long.valueOf(
                        userIdObject.toString()
                );

        List<Notification> notifications =
                notificationRepository
                        .findByUserId(userId);

        return ResponseEntity.ok(notifications);
    }

    // Mark notification as read
    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(
            @PathVariable Long id,
            HttpSession session) {

        Object userIdObject =
                session.getAttribute("userId");

        if (userIdObject == null) {
            return ResponseEntity
                    .status(401)
                    .body("Please login first.");
        }

        Long userId =
                Long.valueOf(
                        userIdObject.toString()
                );

        Notification notification =
                notificationRepository
                        .findById(id)
                        .orElse(null);

        if (notification == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        // Make sure the notification belongs
        // to the logged-in user
        if (!userId.equals(
                notification.getUserId())) {

            return ResponseEntity
                    .status(403)
                    .body("Access denied.");
        }

        notification.setReadStatus(true);

        notificationRepository.save(notification);

        return ResponseEntity.ok(notification);
    }
}