package com.connect.eduHub.controller;

import java.security.PublicKey;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.connect.eduHub.model.User;
import com.connect.eduHub.repository.UserRepo;

@RestController
@RequestMapping("/api/keys")
public class KeysController {

	@Autowired
    private UserRepo userRepository;

    @GetMapping("/{username}")
    public ResponseEntity<String> getPublicKey(@PathVariable String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            byte[] key = userOpt.get().getPublicKey();
            return ResponseEntity.ok(Base64.getEncoder().encodeToString(key));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}