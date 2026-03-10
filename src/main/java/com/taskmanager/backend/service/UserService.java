package com.taskmanager.backend.service;

import com.taskmanager.backend.entity.User;
import com.taskmanager.backend.repository.UserRepository;
import com.taskmanager.backend.dto.ChangePasswordRequestDto;
import com.taskmanager.backend.dto.ProfileInfoDto;
import com.taskmanager.backend.dto.RegisterRequestDto;
import com.taskmanager.backend.dto.UpdateProfilePictureDto;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(RegisterRequestDto request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already in use");
        }

        /*Creating a new user */
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public ProfileInfoDto getUserProfile(String email) {
        return userRepository.findByEmail(email).map(ProfileInfoDto::fromEntity)
                            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void changePassword(String email, ChangePasswordRequestDto request) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new RuntimeException("Doesn't match!");
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    public void uploadProfilePicture(String email, MultipartFile file) throws java.io.IOException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }
    
        //File type validation
        String contentType = file.getContentType();
        if (contentType == null || !List.of("image/jpeg", "image/png", "image/webp").contains(contentType)) {
            throw new IllegalArgumentException("Only JPEG, PNG and WEBP images are allowed");
        }
    
        //File size validation
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("File size must be less than 5MB");
        }
    
        UpdateProfilePictureDto dto = new UpdateProfilePictureDto(file);
    
        user.setProfilePicture(dto.getData());
        user.setProfilePictureType(dto.getContentType());
        userRepository.save(user);
    }

    public ResponseEntity<byte[]> getProfilePicture(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(user.getProfilePictureType()))
            .body(user.getProfilePicture());
    }


}