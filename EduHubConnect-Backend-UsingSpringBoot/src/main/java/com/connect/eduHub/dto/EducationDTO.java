package com.connect.eduHub.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationDTO {
    private Long id;
    private String degree;
    private String institution;
    private String graduationYear;
    private Long userId;
}
