package com.prs.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prs.api.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	public User findByUsername(String username);

	public User findByEmail(String email);

	public User findByVerificationCode(String code);

	public User findByResetPasswordToken(String resetToken);

}
