package com.connect.eduHub.controller;

import com.connect.eduHub.dto.RegisterDTO;
import com.connect.eduHub.e2ee.RSAKeyUtil;
import com.connect.eduHub.model.User;
import com.connect.eduHub.model.request.AuthRequest;
import com.connect.eduHub.model.request.RegisterRequest;
import com.connect.eduHub.model.response.AuthResponse;
import com.connect.eduHub.services.AuthService;
import com.connect.eduHub.services.FileStorageService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    
    @Autowired
    private RSAKeyUtil rsaKeyUtil;

    // ✅ Register endpoint
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("Successfully Registered");
    }

    // ✅ Login endpoint
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    
    
    //
    @PostMapping(value = "/registerwithpic", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerUser(
    		 @RequestPart("user") String request,
            @RequestPart("file") MultipartFile file) {

        try {
//             Convert JSON string to DTO
            RegisterDTO userReq = objectMapper.readValue(request, RegisterDTO.class);

            // Save file
            String imagePath = fileStorageService.storeFile(file);

            // Save user
            User user = new User();
            user.setName(userReq.getName());
            user.setBio(userReq.getBio());
            user.setEmail(userReq.getEmail());
            user.setPassword(passwordEncoder.encode(userReq.getPassword()));
            user.setProfileImage(imagePath);
            		
            rsaKeyUtil.saveUserKeyPair(user);
            authService.registerUserWithProfilePic(user);
            return ResponseEntity.ok("User registered successfully!");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    
    
}
