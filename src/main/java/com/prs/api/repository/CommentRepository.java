package com.prs.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prs.api.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
