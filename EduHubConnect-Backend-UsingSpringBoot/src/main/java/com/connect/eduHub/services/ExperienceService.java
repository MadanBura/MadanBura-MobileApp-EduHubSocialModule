package com.connect.eduHub.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.connect.eduHub.dto.ExperienceDTO;
import com.connect.eduHub.mappers.ExperienceMapper;
import com.connect.eduHub.model.Experience;
import com.connect.eduHub.repository.ExperienceRepository;

@Service
public class ExperienceService {

    @Autowired
    private ExperienceRepository experienceRepo;

    @Autowired
    private ExperienceMapper experienceMapper;

    public List<ExperienceDTO> getUserExperiences(Long userId) {
        return experienceRepo.findByUserId(userId)
                .stream()
                .map(experienceMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ExperienceDTO addExperience(Long userId, ExperienceDTO dto) {
        dto.setId(userId);
        Experience experience = experienceMapper.toEntity(dto);
        return experienceMapper.toDTO(experienceRepo.save(experience));
    }
}