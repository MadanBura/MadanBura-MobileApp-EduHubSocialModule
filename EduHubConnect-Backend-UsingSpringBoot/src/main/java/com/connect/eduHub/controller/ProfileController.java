package com.connect.eduHub.controller;

import com.connect.eduHub.dto.EducationDTO;
import com.connect.eduHub.dto.ExperienceDTO;
import com.connect.eduHub.dto.ProfileUpdateDTO;
import com.connect.eduHub.dto.SkillDTO;
import com.connect.eduHub.dto.UserProfileDTO;
import com.connect.eduHub.model.Address;
import com.connect.eduHub.model.User;
import com.connect.eduHub.repository.UserRepo;
import com.connect.eduHub.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

	@Autowired
    private ProfileService profileService;
	@Autowired UserRepo userRepo;

   
    @GetMapping("/seeUserProfile/{userId}")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable Long userId) {
        UserProfileDTO userProfile = profileService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }
    
    @PatchMapping("/{userId}")
    public ResponseEntity<String> updateProfile(@PathVariable Long userId, @RequestBody ProfileUpdateDTO dto) {
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.getCollegeName() != null)
            user.setCollegeName(dto.getCollegeName());

        if (dto.getBannerImageUrl() != null)
            user.setProfileBackgroundBanner(dto.getBannerImageUrl());

        if (dto.getAddress() != null) {
            Address address = new Address();
            address.setStreet(dto.getAddress().getStreet());
            address.setCity(dto.getAddress().getCity());
            address.setState(dto.getAddress().getState());
            address.setCountry(dto.getAddress().getCountry());
            address.setPincode(dto.getAddress().getPincode());
            address.setUser(user);
            user.setAddress(address);
        }

        userRepo.save(user);
        return ResponseEntity.ok("Profile updated.");
    }

    @PostMapping("/{userId}/experience")
    public ResponseEntity<String> addExperience(@PathVariable Long userId, @RequestBody ExperienceDTO experienceDTO) {
        profileService.addExperience(userId, experienceDTO);
        return ResponseEntity.ok("Experience added successfully.");
    }

    @PostMapping("/{userId}/skill")
    public ResponseEntity<String> addSkill(@PathVariable Long userId, @RequestBody SkillDTO skillDTO) {
        profileService.addSkill(userId, skillDTO);
        return ResponseEntity.ok("Skill added successfully.");
    }

    @PostMapping("/{userId}/education")
    public ResponseEntity<String> addEducation(@PathVariable Long userId, @RequestBody EducationDTO educationDTO) {
        profileService.addEducation(userId, educationDTO);
        return ResponseEntity.ok("Education added successfully.");
    }
    
}
