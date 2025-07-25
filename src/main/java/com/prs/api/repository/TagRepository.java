package com.prs.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prs.api.entity.Tag;

public interface TagRepository  extends JpaRepository<Tag, Long> {

}
