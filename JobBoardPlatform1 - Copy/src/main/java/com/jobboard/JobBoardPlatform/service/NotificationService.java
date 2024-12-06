package com.jobboard.JobBoardPlatform.service;

import com.jobboard.JobBoardPlatform.model.Notification;
import com.jobboard.JobBoardPlatform.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // Method to create and save a notification
    public Notification createNotification(String message) {
        Notification notification = new Notification(message);
        return notificationRepository.save(notification);  // Save notification to DB
    }

    // Method to retrieve all notifications for the admin
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();  // Fetch all notifications
    }

    // Mark notification as read
    public void markAsRead(Long id) {
        Optional<Notification> notificationOpt = notificationRepository.findById(id);
        notificationOpt.ifPresent(notification -> {
            notification.setIsRead(true);
            notificationRepository.save(notification);
        });
    }

    // Clear all notifications
    public void clearAllNotifications() {
        notificationRepository.deleteAll();  // Clear all notifications from the DB
    }
}
