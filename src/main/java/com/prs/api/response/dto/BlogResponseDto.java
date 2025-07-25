package com.prs.api.response.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prs.api.entity.Comment;
import com.prs.api.entity.Tag;
import com.prs.api.entity.User;
import com.prs.api.enums.BlogStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BlogResponseDto {

	private Long id;
	private String title;
	private String slug; // SEO-friendly URL
	private String content;
	private String excerpt;
	private String featuredImageUrl;
	private BlogStatus status = BlogStatus.DRAFT;
	private Set<Tag> tags = new HashSet<>();
	private Set<Comment> comments = new HashSet<>();
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime publishedAt;
	private User author;
	private String metaTitle;
	private String metaDescription;
	private Long version;
}
