package com.connect.eduHub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
	 private String name;
	 private String email;
	 private String password;
	 private String bio;
}
