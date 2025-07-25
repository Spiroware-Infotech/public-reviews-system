package com.prs.api.mapper;

import org.mapstruct.Mapper;

import com.prs.api.entity.Blog;
import com.prs.api.response.dto.BlogResponseDto;

@Mapper(componentModel = "spring")
public interface BlogsMapper {

	BlogResponseDto toDto(Blog blog);
}
