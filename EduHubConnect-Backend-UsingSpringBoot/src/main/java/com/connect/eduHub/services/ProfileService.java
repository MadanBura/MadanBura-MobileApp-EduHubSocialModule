package com.connect.eduHub.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.connect.eduHub.dto.AddressDTO;
import com.connect.eduHub.dto.EducationDTO;
import com.connect.eduHub.dto.ExperienceDTO;
import com.connect.eduHub.dto.PostDTO;
import com.connect.eduHub.dto.SkillDTO;
import com.connect.eduHub.dto.UserProfileDTO;
import com.connect.eduHub.mappers.EducationMapper;
import com.connect.eduHub.mappers.ExperienceMapper;
import com.connect.eduHub.mappers.PostMapper;
import com.connect.eduHub.mappers.SkillMapper;
import com.connect.eduHub.model.User;
import com.connect.eduHub.repository.EducationRepository;
import com.connect.eduHub.repository.ExperienceRepository;
import com.connect.eduHub.repository.PostRepository;
import com.connect.eduHub.repository.SkillRepository;
import com.connect.eduHub.repository.UserRepo;

@Service
public class ProfileService {

    @Autowired private UserRepo userRepo;
    @Autowired private ExperienceRepository experienceRepo;
    @Autowired private EducationRepository educationRepo;
    @Autowired private SkillRepository skillRepo;
    @Autowired private PostRepository postRepo; // Optional

    @Autowired private ExperienceMapper experienceMapper;
    @Autowired private EducationMapper educationMapper;
    @Autowired private SkillMapper skillMapper;
    @Autowired private PostMapper postMapper; // Optional

   public UserProfileDTO getUserProfile(Long userId) {
    User user = userRepo.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));

    List<ExperienceDTO> experiences = experienceRepo.findByUserId(userId)
        .stream().map(experienceMapper::toDTO).toList();

    List<EducationDTO> education = educationRepo.findByUserId(userId)
        .stream().map(educationMapper::toDTO).toList();

    List<SkillDTO> skills = skillRepo.findByUserId(userId)
        .stream().map(skillMapper::toDTO).toList();

    List<PostDTO> posts = postRepo.findPostsByUserId(userId)
        .stream().map(postMapper::toDTO).toList();

    // ✅ Safe Address Mapping
    AddressDTO addressDTO = null;
    if (user.getAddress() != null) {
        addressDTO = new AddressDTO(
            user.getAddress().getId(),
            user.getAddress().getStreet(),
            user.getAddress().getCity(),
            user.getAddress().getState(),
            user.getAddress().getCountry(),
            user.getAddress().getPincode(),
            user.getId()
        );
    }

    return new UserProfileDTO(
        user.getId(),
        user.getName(),
        user.getBio(),
        user.getCollegeName(),
        addressDTO,
        user.getProfileImage(),
        user.getProfileBackgroundBanner(),
        user.getAbout(),
        experiences,
        education,
        skills,
        posts
    );
}
   
   
   
   
   public void addExperience(Long userId, ExperienceDTO experienceDTO) {
	    User user = userRepo.findById(userId)
	        .orElseThrow(() -> new RuntimeException("User not found"));

	    var experience = experienceMapper.toEntity(experienceDTO);
	    experience.setUser(user);
	    experienceRepo.save(experience);
	}

	public void addSkill(Long userId, SkillDTO skillDTO) {
	    User user = userRepo.findById(userId)
	        .orElseThrow(() -> new RuntimeException("User not found"));

	    var skill = skillMapper.toEntity(skillDTO);
	    skill.setUser(user);
	    skillRepo.save(skill);
	}

	public void addEducation(Long userId, EducationDTO educationDTO) {
	    User user = userRepo.findById(userId)
	        .orElseThrow(() -> new RuntimeException("User not found"));

	    var education = educationMapper.toEntity(educationDTO);
	    education.setUser(user);
	    educationRepo.save(education);
	}

}
