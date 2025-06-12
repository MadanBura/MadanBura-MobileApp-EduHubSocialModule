package com.connect.eduHub.mappers;

import com.connect.eduHub.dto.SkillDTO;
import com.connect.eduHub.model.Skill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SkillMapper {

    @Mapping(source = "user.id", target = "userId")
    SkillDTO toDTO(Skill skill);

    @Mapping(source = "userId", target = "user.id")
    Skill toEntity(SkillDTO skillDTO);
}
