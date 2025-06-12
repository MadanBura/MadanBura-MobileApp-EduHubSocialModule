package com.connect.eduHub.mappers;

import com.connect.eduHub.dto.EducationDTO;
import com.connect.eduHub.model.Education;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EducationMapper {

    @Mapping(source = "user.id", target = "userId")
    EducationDTO toDTO(Education education);

    @Mapping(source = "userId", target = "user.id")
    Education toEntity(EducationDTO educationDTO);
}
