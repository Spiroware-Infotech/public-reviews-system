package com.prs.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prs.api.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	
	Optional<Role> findByName(String name);
}
