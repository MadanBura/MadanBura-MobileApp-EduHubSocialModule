package com.connect.eduHub.mappers;

import com.connect.eduHub.dto.AddressDTO;
import com.connect.eduHub.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AddressMapper {

    @Mapping(source = "user.id", target = "userId")
    AddressDTO toDTO(Address address);

    @Mapping(source = "userId", target = "user.id")
    Address toEntity(AddressDTO addressDTO);
}
