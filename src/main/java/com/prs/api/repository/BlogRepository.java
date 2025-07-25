package com.prs.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prs.api.entity.Blog;

public interface BlogRepository  extends JpaRepository<Blog, Long> {

}
