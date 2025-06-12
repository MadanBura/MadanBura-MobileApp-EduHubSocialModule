package com.connect.eduHub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "addresses")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;     // Optional: for more details
    private String city;
    private String state;
    private String country;
    private String pincode;

    @OneToOne(mappedBy = "address")
    private User user; // Optional: Bi-directional link
}
