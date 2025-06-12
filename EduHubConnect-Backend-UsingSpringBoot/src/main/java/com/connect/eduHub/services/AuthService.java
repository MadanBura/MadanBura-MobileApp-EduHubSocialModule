package com.connect.eduHub.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.connect.eduHub.e2ee.RSAKeyUtil;
import com.connect.eduHub.model.User;
import com.connect.eduHub.model.request.AuthRequest;
import com.connect.eduHub.model.request.RegisterRequest;
import com.connect.eduHub.model.response.AuthResponse;
import com.connect.eduHub.repository.UserRepo;
import com.connect.eduHub.secutiry.JWTService;

@Service
public class AuthService {

    @Autowired private UserRepo userRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JWTService jwtService;
    
    @Autowired
    private RSAKeyUtil rsaKeyUtil;


    // ✅ Register user only, no AuthResponse returned
    public void register(RegisterRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setBio(request.getBio());
        user.setProfileImage(request.getProfileImage());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        try {
			rsaKeyUtil.saveUserKeyPair(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
        userRepo.save(user);
       
    }

    // ✅ Login returns AuthResponse with token
    public AuthResponse login(AuthRequest request) {
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(
            new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), new ArrayList<>()
            )
        );

        return new AuthResponse(200,user.getId(), token, "Successfully Logged In");
    }
    
    
    
    public void registerUserWithProfilePic(User user) {
    	userRepo.save(user);
    }
}
