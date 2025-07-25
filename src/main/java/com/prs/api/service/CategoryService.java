package com.prs.api.service;

import java.util.List;

import com.prs.api.response.dto.CategoryDto;

public interface CategoryService {

	public List<CategoryDto> getAllCategories();

	public CategoryDto getCategoryById(Long id);
}