package com.app.secure_user_api.service;

import com.app.secure_user_api.entity.RefreshToken;
import com.app.secure_user_api.entity.User;
import com.app.secure_user_api.repository.RefreshTokenRepository;
import com.app.secure_user_api.repository.UserRepository;
import com.app.secure_user_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository repository;

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final long refreshTokenDurationMs = 7 * 24 * 60 * 60 * 1000; // 7 days

    public RefreshToken createRefreshToken(User user) {

        repository.findByUser(user)
                .ifPresent(repository::delete);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(jwtService.generateToken(user));
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));

        return repository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {

        if (token.getExpiryDate().isBefore(Instant.now())) {
            repository.delete(token);
            throw new RuntimeException("Refresh token expired");
        }

        return token;
    }

    public void deleteByUser(User user) {
        repository.deleteByUser_Id(user.getId());
    }
}