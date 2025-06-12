package com.connect.eduHub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AddressDTO {

	 private Long id;
    private String street;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private Long userId;

 
}
