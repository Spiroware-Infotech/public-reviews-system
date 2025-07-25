package com.prs.api.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prs.api.dto.CommentRequestDto;
import com.prs.api.response.ApiResponse;
import com.prs.api.response.dto.CommentResponse;
import com.prs.api.service.Commentservice;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/blogs")
@Tag(name = "Comments REST API")
public class CommentController {
	@Autowired
	private Commentservice commentService;
	
	@PostMapping("/addComment")
	public ResponseEntity<ApiResponse<CommentResponse>> addComment(@RequestBody CommentRequestDto requestdto){
		try {
		CommentResponse commentResponse=	commentService.addComment(requestdto);
			ApiResponse<CommentResponse> response = new ApiResponse<>(true, "Comment added sucessfully",
					commentResponse,HttpStatus.OK.value(), LocalDateTime.now());
			
			return ResponseEntity.ok(response);
		
		}
		catch(Exception ex) {
			
			ApiResponse<CommentResponse> errorResponse = new ApiResponse<>(false, ex.getMessage(),
					null ,HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());	
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		
		
		
	}
}
