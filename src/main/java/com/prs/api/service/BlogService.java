package com.prs.api.service;

import java.util.List;

import com.prs.api.dto.BlogRequestDto;
import com.prs.api.response.dto.BlogResponseDto;

public interface BlogService {

	BlogResponseDto createBlog(BlogRequestDto blogreq);

	BlogResponseDto getBlogById(long id);

	List<BlogResponseDto> getAllBlogs();

}
