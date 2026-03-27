package com.encrytion.jwe.auth.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 512)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiryAt;

    @Column(nullable = false)
    private boolean revoked;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UserAccount userAccount;
}
