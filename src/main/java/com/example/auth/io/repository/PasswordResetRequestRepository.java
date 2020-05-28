package com.example.auth.io.repository;

import com.example.auth.io.entity.PasswordResetRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetRequestRepository extends JpaRepository<PasswordResetRequestEntity, Long> {
    PasswordResetRequestEntity findByPasswordResetRequestToken(String token);
}
