package com.encrytion.jwe.auth.service;

import com.encrytion.jwe.auth.domain.RefreshToken;
import com.encrytion.jwe.auth.domain.UserAccount;
import com.encrytion.jwe.auth.dto.AuthResponse;
import com.encrytion.jwe.auth.dto.LoginRequest;
import com.encrytion.jwe.auth.dto.RefreshTokenRequest;
import com.encrytion.jwe.auth.dto.RegisterRequest;
import com.encrytion.jwe.auth.repo.UserAccountRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserAccountRepo userAccountRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthResponse register(RegisterRequest request) {
        if (userAccountRepo.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        UserAccount userAccount = new UserAccount();
        userAccount.setFullName(request.fullName());
        userAccount.setEmail(request.email());
        userAccount.setPassword(passwordEncoder.encode(request.password()));
        userAccount = userAccountRepo.save(userAccount);

        return issueTokens(userAccount);
    }

    public AuthResponse login(LoginRequest request) {
        UserAccount userAccount = userAccountRepo.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(request.password(), userAccount.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return issueTokens(userAccount);
    }

    public AuthResponse refresh(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.verify(request.refreshToken());
        return issueTokens(refreshToken.getUserAccount());
    }

    private AuthResponse issueTokens(UserAccount userAccount) {
        RefreshToken refreshToken = refreshTokenService.create(userAccount);
        String accessToken = jwtService.generateAccessToken(
                org.springframework.security.core.userdetails.User.withUsername(userAccount.getEmail())
                        .password(userAccount.getPassword())
                        .authorities(userAccount.getRole())
                        .build()
        );

        return new AuthResponse(
                accessToken,
                refreshToken.getToken(),
                "Bearer",
                jwtService.accessTokenExpirySeconds(),
                userAccount.getEmail(),
                userAccount.getFullName()
        );
    }
}
