package com.jobboard.JobBoardPlatform.repository;

import com.jobboard.JobBoardPlatform.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
    // Additional query methods can be added if needed
}
