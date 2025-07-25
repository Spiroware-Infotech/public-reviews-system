package com.prs.api.service;

import com.prs.api.dto.JwtRequest;
import com.prs.api.dto.JwtResponse;

public interface AuthService {
	
	JwtResponse loginUserService(JwtRequest authRequest) throws Exception;
}
