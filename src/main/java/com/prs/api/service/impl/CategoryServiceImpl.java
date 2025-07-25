package com.prs.api.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prs.api.entity.Category;
import com.prs.api.mapper.CategoryMapper;
import com.prs.api.repository.CategoryRepository;
import com.prs.api.response.dto.CategoryDto;
import com.prs.api.service.CategoryService;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private CategoryMapper categoryMapper;

	@Override
	public List<CategoryDto> getAllCategories() {
		List<Category> categories = categoryRepository.findAllActiveCategories();
		List<CategoryDto> responseList = categories.stream().map(categoryMapper::toDto).collect(Collectors.toList());
		return responseList;
	}

	@Override
	public CategoryDto getCategoryById(Long id) {
		Optional<Category> categoryOpt = categoryRepository.findCategoryActiveById(id);
		if (categoryOpt.isPresent()) {
			CategoryDto category = categoryMapper.toDto(categoryOpt.get());
			return category;
		}
		return null;
	}

}