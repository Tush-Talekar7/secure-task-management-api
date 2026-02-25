package com.app.secure_user_api.service;

import com.app.secure_user_api.dto.RefreshTokenRequestDTO;
import com.app.secure_user_api.dto.RegisterRequestDTO;
import com.app.secure_user_api.entity.RefreshToken;
import com.app.secure_user_api.entity.Role;
import com.app.secure_user_api.entity.User;
import com.app.secure_user_api.exception.BadRequestException;
import com.app.secure_user_api.repository.RefreshTokenRepository;
import com.app.secure_user_api.repository.UserRepository;
import com.app.secure_user_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    private final RefreshTokenRepository refreshTokenRepository;

    public String register(RegisterRequestDTO request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");

        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Username already exists");

        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .enabled(true)
                .build();

        userRepository.save(user);

        return "User registered successfully";
    }

    public String login(String username, String password) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return jwtService.generateToken(user);
    }

    public RefreshTokenRequestDTO getRefreshToken(RefreshTokenRequestDTO refreshDTO){

        String requestToken = refreshDTO.getRefreshToken();

        RefreshToken refreshToken = refreshTokenRepository.findByToken(requestToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        refreshTokenService.verifyExpiration(refreshToken);

        User user = refreshToken.getUser();

        // DELETE OLD TOKEN (Rotation)
        refreshTokenRepository.delete(refreshToken);

        // CREATE NEW TOKENS
        String newAccessToken = jwtService.generateToken(user);
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

        return new RefreshTokenRequestDTO(newAccessToken,newRefreshToken.getToken());

    }

    public String logout(String username){

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        refreshTokenService.deleteByUser(user);

        return "Logout Successful";

    }


}

