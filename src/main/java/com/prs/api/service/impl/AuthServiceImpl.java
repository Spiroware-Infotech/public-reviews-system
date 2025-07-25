package com.prs.api.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prs.api.config.CustomUserDetailsService;
import com.prs.api.config.JwtUtil;
import com.prs.api.dto.JwtRequest;
import com.prs.api.dto.JwtResponse;
import com.prs.api.entity.User;
import com.prs.api.repository.RoleRepository;
import com.prs.api.repository.UserRepository;
import com.prs.api.response.dto.UserResponse;
import com.prs.api.service.AuthService;
import com.prs.api.utils.DtoConverter;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    public JwtResponse loginUserService(JwtRequest authRequest) throws Exception {

        authenticate(authRequest.getUsername(), authRequest.getPassword());
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(authRequest.getUsername());
        String token = jwtUtil.generateToken(userDetails.getUsername());
        User loggedUser = userRepository.findByUsername(authRequest.getUsername());
        if (Objects.isNull(loggedUser)) {
        	loggedUser = userRepository.findByEmail(authRequest.getUsername());
		}
        UserResponse userResponse = DtoConverter.convertToDto(loggedUser, UserResponse.class);
        List<String> roles = new ArrayList<String>();

		for (GrantedAuthority a : userDetails.getAuthorities()) {
			roles.add(a.getAuthority());
		}
		if(roles.size()> 0)
			userResponse.setRole(roles.get(0));
		
        return new JwtResponse(token,"Bearer", userResponse);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
        	if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("Username can not be empty!.");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("Password can not be empty!.");
            }
            
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new DisabledException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", e);
        }
    }
}
