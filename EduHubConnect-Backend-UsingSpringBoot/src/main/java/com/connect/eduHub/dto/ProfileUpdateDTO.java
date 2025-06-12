package com.connect.eduHub.dto;

import lombok.Data;

@Data
public class ProfileUpdateDTO {
    private String collegeName;
    private AddressDTO address;
    private String bannerImageUrl;
}