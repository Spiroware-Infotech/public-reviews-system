package com.prs.api.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prs.api.dto.BlogRequestDto;
import com.prs.api.response.ApiResponse;
import com.prs.api.response.dto.BlogResponseDto;
import com.prs.api.service.BlogService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping("/api/v1/auth")
@RestController
@Tag(name = "Blogs REST API")
public class BlogRestApiControler {

	@Autowired
	private BlogService blogservice;

	@PostMapping("/blogs/createBlog")
	public ResponseEntity<ApiResponse<BlogResponseDto>> createBlog(@RequestBody BlogRequestDto blogreq) {
		try {

			BlogResponseDto responsedto = blogservice.createBlog(blogreq);

			ApiResponse<BlogResponseDto> successResponse = new ApiResponse<>(true, "Blog saved successfully",
					responsedto, HttpStatus.OK.value(), LocalDateTime.now());
			return ResponseEntity.ok(successResponse);
		} catch (Exception ex) {

			ApiResponse<BlogResponseDto> errorResponse = new ApiResponse<>(false, ex.getMessage(), null,
					HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}

	}

	@GetMapping("/blog/{id}")
	public ResponseEntity<ApiResponse<?>> getBlog(@PathVariable long id) {
		BlogResponseDto response = blogservice.getBlogById(id);

		ApiResponse<BlogResponseDto> sucessResponse = new ApiResponse<>(true, "Blog retrived with id ", response,
				HttpStatus.OK.value(), LocalDateTime.now());
		return ResponseEntity.ok(sucessResponse);

	}

	@GetMapping("/blogs/all")
	public ResponseEntity<ApiResponse<List<BlogResponseDto>>> getAllBlogs() {

		List<BlogResponseDto> response = blogservice.getAllBlogs();

		ApiResponse<List<BlogResponseDto>> sucessResponse = new ApiResponse<>(true, "All blogs retrived sucessfully ",
				response, HttpStatus.OK.value(), LocalDateTime.now());

		return ResponseEntity.ok(sucessResponse);

	}

}
