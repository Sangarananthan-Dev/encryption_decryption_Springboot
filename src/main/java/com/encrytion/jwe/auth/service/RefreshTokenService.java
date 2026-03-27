package com.encrytion.jwe.auth.service;

import com.encrytion.jwe.auth.config.JwtProperties;
import com.encrytion.jwe.auth.domain.RefreshToken;
import com.encrytion.jwe.auth.domain.UserAccount;
import com.encrytion.jwe.auth.repo.RefreshTokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepo refreshTokenRepo;
    private final JwtProperties jwtProperties;

    public RefreshToken create(UserAccount userAccount) {
        refreshTokenRepo.deleteByUserAccount(userAccount);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID() + "." + UUID.randomUUID());
        refreshToken.setExpiryAt(LocalDateTime.now().plusDays(jwtProperties.refreshTokenExpirationDays()));
        refreshToken.setRevoked(false);
        refreshToken.setUserAccount(userAccount);
        return refreshTokenRepo.save(refreshToken);
    }

    public RefreshToken verify(String token) {
        RefreshToken refreshToken = refreshTokenRepo.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found"));

        if (refreshToken.isRevoked() || refreshToken.getExpiryAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Refresh token expired or revoked");
        }

        return refreshToken;
    }
}
