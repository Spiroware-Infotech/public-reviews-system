package com.prs.api.user.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prs.api.aspects.TrackExecutionTime;
import com.prs.api.dto.ChangePassword;
import com.prs.api.dto.ReviewRequest;
import com.prs.api.dto.UsersDto;
import com.prs.api.entity.User;
import com.prs.api.exceptions.EmailFoundException;
import com.prs.api.exceptions.ReviewEngagementException;
import com.prs.api.exceptions.ReviewOwnershipException;
import com.prs.api.exceptions.UserFoundException;
import com.prs.api.exceptions.UserNotFountException;
import com.prs.api.response.ApiResponse;
import com.prs.api.response.ReviewResponse;
import com.prs.api.response.dto.UserAllResponse;
import com.prs.api.response.dto.UserResponse;
import com.prs.api.service.ReviewService;
import com.prs.api.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/user")
@PreAuthorize("hasRole('USER')")
@Tag(name = "Users Rest API")
public class UserRestApiController {

	@Autowired
	private UserService userService;

	@Autowired
	private ReviewService reviewService;

	@TrackExecutionTime
	@GetMapping("/dashboard")
	public ResponseEntity<ApiResponse<?>> dashboard(Principal principal) {
		log.info("Welcome to dashboard!..");

		try {
			UserResponse user = userService.findUserByUserName(principal.getName());

			if (user == null) {
				ApiResponse<UserResponse> notFoundResponse = new ApiResponse<>(false, "User not found", null,
						HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
			}

			ApiResponse<UserResponse> successResponse = new ApiResponse<>(true, "User fetched successfully", user,
					HttpStatus.OK.value(), LocalDateTime.now());
			return ResponseEntity.ok(successResponse);

		} catch (Exception e) {
			log.error("Error fetching dashboard user data: {}", e.getMessage());
			ApiResponse<UserResponse> errorResponse = new ApiResponse<>(false, e.getLocalizedMessage(), null,
					HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@TrackExecutionTime
	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponse<?>> getUserById(@PathVariable Long userId) {
		try {
			UserAllResponse user = userService.getUserById(userId);
			ApiResponse<UserAllResponse> response = new ApiResponse<>(true, "User fetched successfully", user,
					HttpStatus.OK.value(), LocalDateTime.now());
			return ResponseEntity.ok(response);

		} catch (UserNotFountException ex) {
			ApiResponse<UserAllResponse> errorResponse = new ApiResponse<>(false, ex.getMessage(), null,
					HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

		} catch (Exception ex) {
			ApiResponse<UserAllResponse> errorResponse = new ApiResponse<>(false, ex.getLocalizedMessage(), null,
					HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@TrackExecutionTime
	@PostMapping("/profileUpdate")
	public ResponseEntity<ApiResponse<?>> updateUserProfile(@Valid @RequestBody UsersDto userRequest,
			HttpServletRequest request) {
		try {
			UserAllResponse user = userService.updateUserProfile(userRequest, request, "ROLE_USER");

			ApiResponse<UserAllResponse> response = new ApiResponse<>(true, "User Updated successfully!", user,
					HttpStatus.CREATED.value(), LocalDateTime.now());

			return ResponseEntity.status(HttpStatus.CREATED).body(response);

		} catch (UserFoundException | EmailFoundException | ConstraintViolationException ex) {
			ApiResponse<?> errorResponse = new ApiResponse<>(false, ex.getMessage(), null, HttpStatus.CONFLICT.value(),
					LocalDateTime.now());

			return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);

		} catch (Exception ex) {
			ApiResponse<?> errorResponse = new ApiResponse<>(false, ex.getLocalizedMessage(), null,
					HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	// Submit a new review (authenticated user)
	@TrackExecutionTime
	@PostMapping("/submitReview")
	public ResponseEntity<ApiResponse<String>> submitReview(@RequestBody @Valid ReviewRequest request,
			Principal principal) {
		try {
			reviewService.submitReview(request, principal.getName());

			ApiResponse<String> response = new ApiResponse<>(true, "Review submitted successfully", null,
					HttpStatus.OK.value());
			return ResponseEntity.ok(response);

		} catch (EntityNotFoundException ex) {
			ApiResponse<String> errorResponse = new ApiResponse<>(false, "User not found", null,
					HttpStatus.NOT_FOUND.value());
			return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

		} catch (Exception ex) {
			ApiResponse<String> errorResponse = new ApiResponse<>(false,
					"An unexpected error occurred: " + ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Update password (authenticated user)
	@TrackExecutionTime
	@PostMapping("/updatePwd/{userId}")
	public ResponseEntity<ApiResponse<String>> updatePwd(@PathVariable Long userId,
			@RequestBody ChangePassword password, Principal principal) {
		log.info("Welcome to cpasswordSubmit!..");
		try {

			User loggedUser = userService.findUserById(userId);
			// 1. Check for class name duplicationuserService
			boolean isPasswordExists = userService.checkExistingPassword(password, userId);
			// 2. Attach appropriate error messages
			if (!isPasswordExists) {
				ApiResponse<String> errorResponse = new ApiResponse<>(false, "The Old Password does not match.", null,
						HttpStatus.BAD_REQUEST.value());
				return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
			}

			boolean sameNewPassword = password.getNew_password().equalsIgnoreCase(password.getRe_password());
			if (!(sameNewPassword)) {
				ApiResponse<String> errorResponse = new ApiResponse<>(false,
						"The Re-Password field does not match the New Password field.", null,
						HttpStatus.BAD_REQUEST.value());
				return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
			}
			userService.updatePassword(loggedUser, password.getNew_password());

			ApiResponse<String> response = new ApiResponse<>(true, "Password Updated successfully!", null,
					HttpStatus.OK.value());
			return ResponseEntity.ok(response);

		} catch (Exception ex) {
			ApiResponse<String> errorResponse = new ApiResponse<>(false,
					"An unexpected error occurred: " + ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	/**
     * Get a single review by ID
     */
    @GetMapping("/review/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> getReviewById(@PathVariable Long reviewId) {
        try {
            ReviewResponse review = reviewService.getReviewById(reviewId);
            if (review == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Review not found with ID: " + reviewId, null, HttpStatus.NOT_FOUND.value()));
            }
            return ResponseEntity.ok(new ApiResponse<>(true, "Fetched review successfully", review, HttpStatus.OK.value()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Review not found: " + e.getMessage(), null, HttpStatus.NOT_FOUND.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Failed to fetch review: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
    
    
    /**
     * Get all reviews for a specific userId
     */
    @GetMapping("/reviews/{userId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviewsByOrganization(@PathVariable Long userId) {
        try {
            List<ReviewResponse> reviews = reviewService.getReviewsByUserId(userId);

            if (reviews.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiResponse<>(true, "No reviews found for the User", Collections.emptyList(), HttpStatus.NO_CONTENT.value()));
            }

            return ResponseEntity.ok(new ApiResponse<>(true, "Fetched Reviews Successfully", reviews, HttpStatus.OK.value()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "UserId not found with ID: " + userId, null, HttpStatus.NOT_FOUND.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error fetching reviews: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
    
	/**
	 * Delete review (optional)
	 */
	@TrackExecutionTime
	@DeleteMapping("/review/delete/{userId}/{reviewId}")
	public ResponseEntity<ApiResponse<String>> deleteReview(@PathVariable Long userId, @PathVariable Long reviewId) {
		try {
			boolean deleted = reviewService.deleteReview(userId, reviewId);
			if (deleted) {
				return ResponseEntity
						.ok(new ApiResponse<>(true, "Review deleted successfully", null, HttpStatus.OK.value()));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ApiResponse<>(false, "Review not found", null, HttpStatus.NOT_FOUND.value()));
			}
		} catch (AccessDeniedException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(new ApiResponse<>(false, e.getMessage(), null, HttpStatus.FORBIDDEN.value()));
		} catch (ReviewOwnershipException e) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
					.body(new ApiResponse<>(false, e.getMessage(), null, HttpStatus.NOT_ACCEPTABLE.value()));
		} catch (ReviewEngagementException e) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
					.body(new ApiResponse<>(false, e.getMessage(), null, HttpStatus.NOT_ACCEPTABLE.value()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new ApiResponse<>(false, "Error deleting review", null, HttpStatus.INTERNAL_SERVER_ERROR.value()));
		}
	}

    
}
