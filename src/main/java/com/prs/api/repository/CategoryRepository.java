package com.prs.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.prs.api.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	@Query(value = "SELECT * FROM category WHERE id=:catId  and enabled = true", nativeQuery = true)
	Optional<Category> findCategoryActiveById(Long catId);

	@Query(value = "SELECT * FROM category WHERE enabled = true", nativeQuery = true)
	List<Category> findAllActiveCategories();
}
