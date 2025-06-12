package com.connect.eduHub.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.connect.eduHub.dto.LikeDTO;
import com.connect.eduHub.model.Like;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LikeMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "post.id", target = "postId")
    LikeDTO toDTO(Like like);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "postId", target = "post.id")
    Like toEntity(LikeDTO likeDTO);
}
