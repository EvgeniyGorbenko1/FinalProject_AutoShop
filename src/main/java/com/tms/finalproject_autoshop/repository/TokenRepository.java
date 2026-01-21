package com.tms.finalproject_autoshop.repository;

import com.tms.finalproject_autoshop.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<VerificationToken, Integer> {
    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findTokenBySecurityUsername(String username);
}
