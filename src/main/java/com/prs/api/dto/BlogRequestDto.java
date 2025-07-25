package com.prs.api.dto;

import java.util.Set;

import com.prs.api.enums.BlogStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BlogRequestDto {

	private String title;
    private String slug;
    private String content;
    private String excerpt;
    private String featuredImageUrl;
    private BlogStatus status;
    private Set<Long> tagIds;
    private Long authorId;
    private String metaTitle;
    private String metaDescription;
  
    
}
