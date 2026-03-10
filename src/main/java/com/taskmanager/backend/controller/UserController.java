package com.taskmanager.backend.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestBody;

import com.taskmanager.backend.dto.ChangePasswordRequestDto;
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


    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(Authentication authentication, @RequestBody ChangePasswordRequestDto request) {
    
        if(authentication == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            service.changePassword(authentication.getName(), request);
            return ResponseEntity.ok("Your password has been changed successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadProfilePicture(Authentication authentication, 
                                                    @RequestParam("file") MultipartFile file) throws java.io.IOException {

        if(authentication == null) {
            return ResponseEntity.status(401).build();
        }                                                
        service.uploadProfilePicture(authentication.getName(), file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile-picture")
    public ResponseEntity<byte[]> getProfilePicture(Authentication authentication) {
        if(authentication == null) {
            return ResponseEntity.status(401).build();
        }    
        return service.getProfilePicture(authentication.getName());
    }


}
