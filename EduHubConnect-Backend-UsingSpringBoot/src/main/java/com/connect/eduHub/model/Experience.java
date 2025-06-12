package com.connect.eduHub.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Experience {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jobTitle;
    private String companyName;
    private String startDate;
    private String endDate;
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}