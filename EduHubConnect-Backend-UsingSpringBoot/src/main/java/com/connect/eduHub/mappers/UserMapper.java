package com.connect.eduHub.mappers;

import com.connect.eduHub.dto.UserDTO;
import com.connect.eduHub.dto.UserProfileDTO;
import com.connect.eduHub.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserDTO toDTO(User user);
    User toEntity(UserDTO userDTO);
    
    
    UserProfileDTO toDTOO(User user);
    User toEntity(UserProfileDTO userProfileDTO);
    
}
