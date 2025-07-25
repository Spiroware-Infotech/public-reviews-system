package com.prs.api.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prs.api.response.ApiResponse;
import com.prs.api.response.ReviewResponse;
import com.prs.api.service.ReviewService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/auth/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews Rest API")
public class ReviewController {

    private final ReviewService reviewService;


    /**
     * Get all reviews for a specific organization
     */
    @GetMapping("/organization/{orgId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviewsByOrganization(@PathVariable Long orgId) {
        try {
            List<ReviewResponse> reviews = reviewService.getReviewsByOrganizationId(orgId);

            if (reviews.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiResponse<>(true, "No reviews found for the organization", Collections.emptyList(), HttpStatus.NO_CONTENT.value()));
            }

            return ResponseEntity.ok(new ApiResponse<>(true, "Fetched reviews successfully", reviews, HttpStatus.OK.value()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Organization not found with ID: " + orgId, null, HttpStatus.NOT_FOUND.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error fetching reviews: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }


    /**
     * Get all reviews
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getAllReviews() {
        try {
            List<ReviewResponse> reviews = reviewService.getAllReviews();

            if (reviews.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiResponse<>(true, "No reviews found", Collections.emptyList(), HttpStatus.NO_CONTENT.value()));
            }

            return ResponseEntity.ok(new ApiResponse<>(true, "Fetched all reviews successfully", reviews, HttpStatus.OK.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Failed to fetch reviews: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

}