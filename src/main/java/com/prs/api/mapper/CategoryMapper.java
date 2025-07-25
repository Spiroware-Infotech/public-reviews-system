package com.prs.api.mapper;

import org.mapstruct.Mapper;

import com.prs.api.entity.Category;
import com.prs.api.response.dto.CategoryDto;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

	CategoryDto toDto(Category category);
}
