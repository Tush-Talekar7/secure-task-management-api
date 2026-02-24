package com.app.secure_user_api.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/me")
    public String getProfile(Authentication authentication) {
        return "Logged in user: " + authentication.getName();
    }
}

