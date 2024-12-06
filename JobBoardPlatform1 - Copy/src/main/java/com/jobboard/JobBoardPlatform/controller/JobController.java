package com.jobboard.JobBoardPlatform.controller;

import com.jobboard.JobBoardPlatform.model.Job;
import com.jobboard.JobBoardPlatform.service.JobService;
import com.jobboard.JobBoardPlatform.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/employer")
public class JobController {

    private static final Logger logger = LoggerFactory.getLogger(JobController.class);

    private final JobService jobService;
    private final NotificationService notificationService;

    @Autowired
    public JobController(JobService jobService, NotificationService notificationService) {
        this.jobService = jobService;
        this.notificationService = notificationService;
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<Job>> getAllJobs() {
        try {
            List<Job> jobs = jobService.getAllJobs();
            return ResponseEntity.ok(jobs);
        } catch (Exception e) {
            logger.error("Error fetching jobs", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/jobs")
    public ResponseEntity<?> postJob(@RequestBody Job job) {
        try {
            logger.info("Received job: {}", job);

            // Validate required fields
            if (job.getTitle() == null || job.getDescription() == null || job.getLocation() == null || job.getJobType() == null) {
                logger.error("Missing required job fields");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing required job fields.");
            }

            if (job.getIsRead() == null) {
                job.setIsRead(false); // Default value for isRead
            }

            logger.info("Posting new job: {}", job.getTitle());
            Job createdJob = jobService.saveJob(job);

            notificationService.createNotification("A new job has been posted: " + job.getTitle());
            logger.info("Job posted successfully: {}", createdJob);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdJob);
        } catch (Exception e) {
            logger.error("Error posting job: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error posting job: " + e.getMessage());
        }
    }




    @PutMapping("/jobs/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody Job job) {
        try {
            Job existingJob = jobService.getJobById(id);
            if (existingJob == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            job.setId(id);
            Job updatedJob = jobService.saveJob(job);
            return ResponseEntity.ok(updatedJob);
        } catch (Exception e) {
            logger.error("Error updating job", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) {
        try {
            Job job = jobService.getJobById(id);
            if (job == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found.");
            }

            jobService.deleteJob(id);
            return ResponseEntity.ok("Job deleted successfully.");
        } catch (Exception e) {
            logger.error("Error deleting job", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete job: " + e.getMessage());
        }
    }
}
