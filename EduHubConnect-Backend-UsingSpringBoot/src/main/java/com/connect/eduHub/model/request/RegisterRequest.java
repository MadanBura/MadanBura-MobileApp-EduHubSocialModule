package com.connect.eduHub.model.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String bio;
    private String profileImage=""; 
}