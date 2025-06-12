package com.connect.eduHub.model.response;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AuthResponse {
	private int code;
	private long userId;
	private String token;
	private String message;
}