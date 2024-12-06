package com.jobboard.JobBoardPlatform.service;

import com.jobboard.JobBoardPlatform.model.Job;
import com.jobboard.JobBoardPlatform.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    // Save or update a job
    public Job saveJob(Job job) {
        return jobRepository.save(job);
    }

    // Get all jobs
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    // Get a job by ID
    public Job getJobById(Long id) {
        Optional<Job> job = jobRepository.findById(id);
        return job.orElse(null);  // Return null if not found
    }

    // Delete a job by ID
    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }
}
