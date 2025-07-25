package com.prs.api.org.controller;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prs.api.aspects.TrackExecutionTime;
import com.prs.api.dto.ChangePassword;
import com.prs.api.dto.LocationData;
import com.prs.api.dto.OrganizationDto;
import com.prs.api.entity.User;
import com.prs.api.exceptions.OrganizationNotFoundException;
import com.prs.api.exceptions.UserFoundException;
import com.prs.api.org.service.OrganizationService;
import com.prs.api.response.ApiResponse;
import com.prs.api.response.dto.OrganizationResponse;
import com.prs.api.response.dto.UserResponse;
import com.prs.api.service.LocationService;
import com.prs.api.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/org")
@PreAuthorize("hasRole('ORGANIZATION')")
@Tag(name = "Organizations Rest API")
@RequiredArgsConstructor
public class OrganizationRestController {

	private final UserService userService;
	private final LocationService locationService;

	private final OrganizationService organizationService;

	@TrackExecutionTime
	@GetMapping("/dashboard")
	public ResponseEntity<ApiResponse<?>> dashboard(Principal principal) {
		log.info("Welcome to dashboard!..");

		try {
			UserResponse userResponse = userService.findUserByUserName(principal.getName());

			if (userResponse == null) {
				ApiResponse<UserResponse> notFoundResponse = new ApiResponse<>(false, "Organization not found", null,
						HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
			}

			ApiResponse<UserResponse> successResponse = new ApiResponse<>(true, "Organization fetched successfully", userResponse,
					HttpStatus.OK.value(), LocalDateTime.now());
			return ResponseEntity.ok(successResponse);

		} catch (Exception e) {
			log.error("Error fetching dashboard user data: {}", e.getMessage());
			ApiResponse<UserResponse> errorResponse = new ApiResponse<>(false, e.getLocalizedMessage(), null,
					HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	// Find Org with normal role
	@TrackExecutionTime
	@GetMapping("/{orgId}")
	public ResponseEntity<ApiResponse<?>> getOrganizationById(@PathVariable Long orgId) {
		try {
			OrganizationResponse organization = organizationService.getOrganizationById(orgId); // implement this in your service
			ApiResponse<OrganizationResponse> response = new ApiResponse<>(true, "Organization fetched successfully",
					organization, HttpStatus.OK.value(), LocalDateTime.now());
			return ResponseEntity.ok(response);

		} catch (OrganizationNotFoundException ex) {
			ApiResponse<OrganizationResponse> errorResponse = new ApiResponse<>(false, ex.getMessage(), null,
					HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

		} catch (Exception ex) {
			ApiResponse<OrganizationResponse> errorResponse = new ApiResponse<>(false, ex.getLocalizedMessage(), null,
					HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	// Update password (authenticated user)
	@TrackExecutionTime
	@PostMapping("/updatePwd/{orgId}")
	public ResponseEntity<ApiResponse<String>> updatePwd(@PathVariable Long orgId,
			@RequestBody ChangePassword password, Principal principal) {
		log.info("Welcome to cpasswordSubmit!..");
		try {

			User loggedUser = userService.findUserById(orgId);
			// 1. Check for class name duplicationuserService
			boolean isPasswordExists = userService.checkExistingPassword(password, orgId);
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
		
	@TrackExecutionTime
	@PostMapping("/profileUpdate")
	public ResponseEntity<ApiResponse<?>> updateUserProfile(@Valid @RequestBody OrganizationDto organizationDto,
			HttpServletRequest request) {
		try {
			OrganizationResponse user = userService.updateOrgProfile(organizationDto, request, "ROLE_ORGANIZATION");

			ApiResponse<OrganizationResponse> response = new ApiResponse<>(true, "Organization Updated successfully!", user,
					HttpStatus.CREATED.value(), LocalDateTime.now());

			return ResponseEntity.status(HttpStatus.CREATED).body(response);

		} catch (UserFoundException | ConstraintViolationException ex) {
			ApiResponse<?> errorResponse = new ApiResponse<>(false, ex.getMessage(), null, HttpStatus.CONFLICT.value(),
					LocalDateTime.now());

			return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);

		} catch (Exception ex) {
			ApiResponse<?> errorResponse = new ApiResponse<>(false, ex.getLocalizedMessage(), null,
					HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	
	@TrackExecutionTime
	@GetMapping("/geolocation/{pincode}")
	public Mono<ResponseEntity<ApiResponse<LocationData>>> getByPincode(@PathVariable String pincode) {
		return locationService.getLocationByPincode(pincode)
				.map(locationData -> ResponseEntity.ok(new ApiResponse<>(true, "Location fetched successfully",
						locationData, HttpStatus.OK.value(), LocalDateTime.now())))
				.defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ApiResponse<>(false, "Location not found for pincode: " + pincode, null,
								HttpStatus.NOT_FOUND.value(), LocalDateTime.now())));
	}
}
