package com.taskmanager.backend.controller;

import com.taskmanager.backend.service.UserService;
import com.taskmanager.backend.dto.RegisterRequestDto;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequestDto request) {
        userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
    
