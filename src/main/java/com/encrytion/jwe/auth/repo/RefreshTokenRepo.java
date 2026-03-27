package com.encrytion.jwe.auth.repo;

import com.encrytion.jwe.auth.domain.RefreshToken;
import com.encrytion.jwe.auth.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUserAccount(UserAccount userAccount);
}
