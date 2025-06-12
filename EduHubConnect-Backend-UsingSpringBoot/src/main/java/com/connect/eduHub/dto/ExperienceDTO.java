package com.connect.eduHub.dto;

import lombok.*;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceDTO {
    private Long id;
    private String jobTitle;
    private String companyName;
    private String startDate;
    private String endDate;
    private String description;
    private Long userId;
}

