package com.connect.eduHub.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.connect.eduHub.dto.MessageDTO;
import com.connect.eduHub.model.Message;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MessageMapper {

    @Mapping(source = "sender.id", target = "senderId")
    @Mapping(source = "receiver.id", target = "receiverId")
    @Mapping(source = "sentAt", target = "timestamp") // ✅ add this line
    MessageDTO toDTO(Message message);

    @Mapping(source = "senderId", target = "sender.id")
    @Mapping(source = "receiverId", target = "receiver.id")
    @Mapping(source = "timestamp", target = "sentAt") // ✅ reverse mapping
    Message toEntity(MessageDTO dto);
}
