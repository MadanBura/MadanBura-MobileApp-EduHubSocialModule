package com.connect.eduHub.mappers;

import com.connect.eduHub.dto.ExperienceDTO;
import com.connect.eduHub.model.Experience;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExperienceMapper {

    @Mapping(source = "user.id", target = "userId")
    ExperienceDTO toDTO(Experience experience);

    @Mapping(source = "userId", target = "user.id")
    Experience toEntity(ExperienceDTO experienceDTO);
}
