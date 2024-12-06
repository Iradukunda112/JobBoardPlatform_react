package com.jobboard.JobBoardPlatform.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification") // Explicitly specify the table name
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // Ensure message is mandatory
    private String message;

    @Column(name = "timestamp", nullable = false, updatable = false) // Make timestamp non-null and immutable
    private LocalDateTime timestamp;

    @Column(name = "is_read", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE") // Use descriptive name
    private boolean isRead;

    // Default constructor for JPA
    public Notification() {
        this.timestamp = LocalDateTime.now();
        this.isRead = false; // Default to unread
    }

    // Convenience constructor
    public Notification(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.isRead = false; // Default to unread
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", isRead=" + isRead +
                '}';
    }
}
