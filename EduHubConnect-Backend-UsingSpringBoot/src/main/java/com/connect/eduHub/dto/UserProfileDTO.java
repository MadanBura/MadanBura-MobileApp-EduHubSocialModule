package com.connect.eduHub.dto;

import java.util.List;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private Long userId;
    private String userName;
    private String bio;
    private String collageName;
    private AddressDTO addressDTO;
    private String profileImageUrl;
    private String bannerImageUrl;
    private String about;

    private List<ExperienceDTO> experiences;
    private List<EducationDTO> educationList;
    private List<SkillDTO> skills;
   
    private List<PostDTO> selfPosts;
}
