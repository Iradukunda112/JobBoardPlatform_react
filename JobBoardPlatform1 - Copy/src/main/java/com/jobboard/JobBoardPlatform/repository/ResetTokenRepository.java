package com.jobboard.JobBoardPlatform.repository;

import com.jobboard.JobBoardPlatform.model.ResetToken;
import com.jobboard.JobBoardPlatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {
    void deleteByToken(String token);
    Optional<ResetToken> findByUser(User user);
    Optional<ResetToken> findByToken(String token);
}
