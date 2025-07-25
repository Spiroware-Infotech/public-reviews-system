package com.prs.api.org.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.prs.api.entity.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long>{

	@Query("SELECT o FROM Organization o " +
		       "LEFT JOIN FETCH o.subscription " +
		       "LEFT JOIN FETCH o.category " +
		       "WHERE o.id = :id")
	Organization findOrganizationById(Long id);

//	@Query("SELECT o FROM Organization o " +
//		       "JOIN FETCH o.category " +
//		       "JOIN FETCH o.subscription " +
//		       "WHERE o.category.name = :category")
	@Query(value = "SELECT o.* FROM organization o " +
            "JOIN category c ON o.cat_id = c.id " +
            "JOIN subscription s ON o.plan_id = s.sub_id " +
            "WHERE c.name = :category", nativeQuery = true)
	List<Organization> findByCategoryName(String category);

}
