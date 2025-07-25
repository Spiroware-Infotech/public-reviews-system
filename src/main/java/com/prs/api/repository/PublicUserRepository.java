package com.prs.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prs.api.entity.PublicUser;

public interface PublicUserRepository  extends JpaRepository<PublicUser, Long> {
	
	public PublicUser findByEmail(String email);
}
