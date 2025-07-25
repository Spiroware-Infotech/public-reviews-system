package com.prs.api.dto;

import com.prs.api.response.dto.UserResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
	private String accessToken;
	private String tokenType = "Bearer";
	private UserResponse user;
    
}
