package com.connect.eduHub.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.connect.eduHub.dto.FriendRequestDTO;
import com.connect.eduHub.model.FriendRequest;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FriendRequestMapper {

	@Mapping(source = "sender.id", target = "senderId")
    @Mapping(source = "receiver.id", target = "receiverId")
	@Mapping(source = "sender.name", target = "senderName")
    @Mapping(source = "sender.email", target = "senderEmail")
    @Mapping(source = "sender.bio", target = "userBio")
    @Mapping(source = "sender.profileImage", target = "senderProfileImage")
    FriendRequestDTO toDTO(FriendRequest friendRequest);

    @Mapping(source = "senderId", target = "sender.id")
    @Mapping(source = "receiverId", target = "receiver.id")
    FriendRequest toEntity(FriendRequestDTO dto);
}
