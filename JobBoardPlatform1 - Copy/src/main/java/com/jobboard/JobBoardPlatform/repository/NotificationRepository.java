package com.jobboard.JobBoardPlatform.repository;

import com.jobboard.JobBoardPlatform.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
