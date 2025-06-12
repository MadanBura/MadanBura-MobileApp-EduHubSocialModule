package com.connect.eduHub.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.connect.eduHub.dto.CommentDTO;
import com.connect.eduHub.model.Comment;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CommentMapper {

	@Mapping(source = "user.id", target = "userId")
	@Mapping(source = "post.id", target = "postId")
	CommentDTO toDTO(Comment comment);

	@Mapping(source = "userId", target = "user.id")
	@Mapping(source = "postId", target = "post.id")
	Comment toEntity(CommentDTO commentDTO);
}
