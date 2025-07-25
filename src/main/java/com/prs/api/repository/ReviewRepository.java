package com.prs.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.prs.api.entity.Reviews;

public interface ReviewRepository extends JpaRepository<Reviews, Long> {

	List<Reviews> findByOrganizationId(Long orgId);

	@Query("SELECT r FROM Reviews r WHERE r.publicUser.id = :userId")
	List<Reviews> findByPublicUserId(@Param("userId") Long userId);
	
	@Modifying
    @Transactional
    @Query(value = "DELETE FROM reviews WHERE rev_id = :id", nativeQuery = true)
    int deleteReviewById(@Param("id") Long id);
}