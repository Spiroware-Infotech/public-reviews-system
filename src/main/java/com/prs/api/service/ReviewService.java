package com.prs.api.service;

import java.util.List;

import com.prs.api.dto.ReviewRequest;
import com.prs.api.response.ReviewResponse;

public interface ReviewService {

	void submitReview(ReviewRequest reviewRequest, String name) throws Exception;

	boolean deleteReview(Long userId, Long reviewId) throws Exception;

	List<ReviewResponse> getAllReviews();

	List<ReviewResponse> getReviewsByOrganizationId(Long orgId);

	ReviewResponse getReviewById(Long id);

	List<ReviewResponse> getReviewsByUserId(Long userId);

}
