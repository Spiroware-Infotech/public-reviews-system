package com.prs.api.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.prs.api.entity.Role;
import com.prs.api.entity.User;
import com.prs.api.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username){
		
		User user = userRepository.findByUsername(username);
		if (Objects.isNull(user)) {
			user = userRepository.findByEmail(username);
		}
		log.info("Login Username : {} ", username);
		
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				user.isEnabled(), true, true, true, getGrantedAuthorities(user));
	}
	
	private List<GrantedAuthority> getGrantedAuthorities(User user) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (Role userProfile : user.getRoles()) {
			System.out.println("Role : " + userProfile);
			authorities.add(new SimpleGrantedAuthority(userProfile.getName()));
		}
		log.info("roles : {}", authorities);
		return authorities;
	}
}