package com.taskmanager.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.backend.dto.ProfileInfoDto;
import com.taskmanager.backend.service.UserService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }
    @GetMapping("/me")
    public ResponseEntity<ProfileInfoDto> getProfile(Authentication authentication) {
        String email = authentication.getName(); 
        return ResponseEntity.ok(service.getUserProfile(email));
    }
}
