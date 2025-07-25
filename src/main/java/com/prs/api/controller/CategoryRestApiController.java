package com.prs.api.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prs.api.aspects.TrackExecutionTime;
import com.prs.api.entity.Category;
import com.prs.api.response.ApiResponse;
import com.prs.api.response.dto.CategoryDto;
import com.prs.api.service.CategoryService;

import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Category REST API")
public class CategoryRestApiController {

	@Autowired
	private CategoryService categoryService;

	@TrackExecutionTime
	@GetMapping("/category/all")
	public ResponseEntity<ApiResponse<?>> getAllActiveCategories() {
		try {
			List<CategoryDto> categories = categoryService.getAllCategories();

			if (categories.size() == 0) {
				ApiResponse<?> notFoundResponse = new ApiResponse<>(false, "No categories found", new ArrayList<>(),
						HttpStatus.NOT_FOUND.value());
				return new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND);
			}

			ApiResponse<List<CategoryDto>> response = new ApiResponse<>(true, "Categories fetched successfully",
					categories, HttpStatus.OK.value(), LocalDateTime.now());
			return ResponseEntity.ok(response);

		} catch (Exception ex) {
			ApiResponse<List<Category>> errorResponse = new ApiResponse<>(false, ex.getMessage(), null,
					HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@TrackExecutionTime
	@GetMapping("/category/{catId}")
	public ResponseEntity<ApiResponse<?>> getActiveCategory(@PathVariable Long catId) {

		try {
			CategoryDto category = categoryService.getCategoryById(catId);

			if (Objects.isNull(category)) {
				ApiResponse<?> notFoundResponse = new ApiResponse<>(false, "No category found", null,
						HttpStatus.NOT_FOUND.value());
				return new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND);
			}

			ApiResponse<CategoryDto> response = new ApiResponse<>(true, "category fetched sucessfully", category,
					HttpStatus.OK.value(), LocalDateTime.now());

			return ResponseEntity.ok(response);
		} catch (Exception ex) {
			ApiResponse<Category> errorResponse = new ApiResponse<>(false, ex.getMessage(), null,
					HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

}
