package com.jobboard.JobBoardPlatform.controller;

import com.jobboard.JobBoardPlatform.model.Notification;
import com.jobboard.JobBoardPlatform.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Endpoint to create a new notification
    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestParam String message) {
        Notification notification = notificationService.createNotification(message);
        return new ResponseEntity<>(notification, HttpStatus.CREATED);
    }

    // Endpoint to get all notifications
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationService.getAllNotifications();
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    // Endpoint to mark a notification as read
    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Endpoint to clear all notifications
    @DeleteMapping
    public ResponseEntity<Void> clearAllNotifications() {
        notificationService.clearAllNotifications();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
