package com.connect.eduHub.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.connect.eduHub.dto.PostDTO;
import com.connect.eduHub.model.Post;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PostMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "userName") // ✅ Add this line
    PostDTO toDTO(Post post);

    @Mapping(source = "userId", target = "user.id")
    Post toEntity(PostDTO postDTO);
}