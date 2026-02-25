package com.app.secure_user_api.controller;


import com.app.secure_user_api.dto.AuthResponseDTO;
import com.app.secure_user_api.dto.LoginRequestDTO;
import com.app.secure_user_api.dto.RefreshTokenRequestDTO;
import com.app.secure_user_api.dto.RegisterRequestDTO;
import com.app.secure_user_api.response.ApiResponse;
import com.app.secure_user_api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody RegisterRequestDTO request) {

        authService.register(request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request) {

        String token = authService.login(
                request.getUsername(),
                request.getPassword()
        );

        return ResponseEntity.ok(
                AuthResponseDTO.builder()
                        .token(token)
                        .build()
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDTO request) {
        return ResponseEntity.ok(new ApiResponse<>(true,"Success",
                authService.getRefreshToken(request)));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(Authentication authentication) {

        return ResponseEntity.ok(new ApiResponse<>(true,"Success",
                authService.logout(authentication.getName())));
    }
}

