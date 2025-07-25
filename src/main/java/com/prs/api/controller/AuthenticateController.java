package com.prs.api.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prs.api.aspects.TrackExecutionTime;
import com.prs.api.config.CustomUserDetailsService;
import com.prs.api.config.JwtUtil;
import com.prs.api.dto.JwtRequest;
import com.prs.api.dto.JwtResponse;
import com.prs.api.dto.OrganizationDto;
import com.prs.api.dto.UsersDto;
import com.prs.api.entity.User;
import com.prs.api.exceptions.EmailFoundException;
import com.prs.api.exceptions.UserFoundException;
import com.prs.api.response.ApiResponse;
import com.prs.api.response.dto.OrganizationResponse;
import com.prs.api.response.dto.UserAllResponse;
import com.prs.api.response.dto.UserResponse;
import com.prs.api.service.AuthService;
import com.prs.api.service.UserService;
import com.prs.api.utils.DtoConverter;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authenticate REST API")
public class AuthenticateController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserService userService;

	@Autowired
	AuthService authService;

	@TrackExecutionTime
	@GetMapping("/welcome")
	public ResponseEntity<ApiResponse<String>> welcome() {
		ApiResponse<String> response = new ApiResponse<>(true, "Welcome, this endpoint is not secure", null,
				HttpStatus.OK.value(), LocalDateTime.now());
		return ResponseEntity.ok(response);
	}

	@TrackExecutionTime
	@PostMapping("/generateToken")
	public ResponseEntity<ApiResponse<JwtResponse>> generateToken(@RequestBody JwtRequest jwtRequest) {
		try {
			authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());

			UserDetails user = customUserDetailsService.loadUserByUsername(jwtRequest.getUsername());
			String token = jwtUtil.generateToken(user.getUsername());
			
			User loggedUser = userService.findByUsername(jwtRequest.getUsername());
			if (Objects.isNull(loggedUser)) {
	        	loggedUser = userService.findByEmail(jwtRequest.getUsername());
			}
	        UserResponse userResponse = DtoConverter.convertToDto(loggedUser, UserResponse.class);
	        List<String> roles = new ArrayList<String>();

			for (GrantedAuthority a : user.getAuthorities()) {
				roles.add(a.getAuthority());
			}
			if(roles.size()> 0)
				userResponse.setRole(roles.get(0));
			
			JwtResponse jwtResponse = new JwtResponse(token, "Bearer", userResponse);

			ApiResponse<JwtResponse> response = new ApiResponse<>(true, "Token generated successfully", jwtResponse,
					HttpStatus.OK.value(), LocalDateTime.now());

			return ResponseEntity.ok(response);

		} catch (UsernameNotFoundException ex) {
			ApiResponse<JwtResponse> response = new ApiResponse<>(false, "User not found", null,
					HttpStatus.NOT_FOUND.value(), LocalDateTime.now());

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

		} catch (BadCredentialsException ex) {
			ApiResponse<JwtResponse> response = new ApiResponse<>(false, "Invalid username or password", null,
					HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now());

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

		} catch (DisabledException ex) {
			ApiResponse<JwtResponse> response = new ApiResponse<>(false, "User is disabled!", null,
					HttpStatus.UNAUTHORIZED.value());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

		} catch (Exception ex) {
			ApiResponse<JwtResponse> response = new ApiResponse<>(false, ex.getLocalizedMessage(), null,
					HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	// Creating user with normal role
	@TrackExecutionTime
	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<?>> createUser(@Valid @RequestBody UsersDto userRequest,
			HttpServletRequest request) {
		try {
			UserAllResponse user = userService.saveUser(userRequest, request, "ROLE_USER");

			ApiResponse<UserAllResponse> response = new ApiResponse<>(true, "User registered successfully", user,
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

	@TrackExecutionTime
	@PostMapping("/org/signup")
	public ResponseEntity<ApiResponse<?>> saveOrganizationUser(@Valid @RequestBody OrganizationDto organizationDto,
			HttpServletRequest request) {
		try {
			OrganizationResponse organization = userService.saveOrganizationUser(organizationDto, "ORG", request);

			ApiResponse<OrganizationResponse> response = new ApiResponse<>(true, "Organization User registered successfully",
					organization, HttpStatus.CREATED.value(), LocalDateTime.now());

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

	// Verify user
	@TrackExecutionTime
	@GetMapping("/verify")
	public ResponseEntity<ApiResponse<String>> verifyAccount(@RequestParam("code") String code) {
		try {
			boolean isVerified = userService.verify(code);
			if (isVerified) {
				ApiResponse<String> response = new ApiResponse<>(true, "Account verified successfully",
						"verify_success", HttpStatus.OK.value(), LocalDateTime.now());
				return ResponseEntity.ok(response);
			} else {
				ApiResponse<String> response = new ApiResponse<>(false, "Verification failed. Invalid or expired code.",
						"verify_fail", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}
		} catch (Exception ex) {
			ApiResponse<String> response = new ApiResponse<>(false, ex.getLocalizedMessage(),
					null, HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	@TrackExecutionTime
	@GetMapping("/resend/verify_code")
	public ResponseEntity<ApiResponse<String>> resendVerifyAccount(@RequestParam("email") String email,HttpServletRequest request) {
		try {
			boolean isVerified = userService.resendVerify(email,request);
			if (isVerified) {
				ApiResponse<String> response = new ApiResponse<>(true, "Account verification Code Sent successfully!",
						"resend_verify_success", HttpStatus.OK.value(), LocalDateTime.now());
				return ResponseEntity.ok(response);
			} else {
				ApiResponse<String> response = new ApiResponse<>(false, "Verification Code Sent failed. Invalid or expired code.",
						"resend_verify_fail", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}
		} catch (Exception ex) {
			ApiResponse<String> response = new ApiResponse<>(false, ex.getLocalizedMessage(),
					null, HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@TrackExecutionTime
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<JwtResponse>> loginUser(@RequestBody JwtRequest loginRequest) {
		try {
			JwtResponse jwtResponse = authService.loginUserService(loginRequest);

			ApiResponse<JwtResponse> response = new ApiResponse<>(true, "Login successful", jwtResponse,
					HttpStatus.OK.value());
			return ResponseEntity.ok(response);

		} catch (UsernameNotFoundException ex) {
			ApiResponse<JwtResponse> response = new ApiResponse<>(false, "User not found", null,
					HttpStatus.NOT_FOUND.value());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

		} catch (BadCredentialsException ex) {
			ApiResponse<JwtResponse> response = new ApiResponse<>(false, "Invalid username or password", null,
					HttpStatus.UNAUTHORIZED.value());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

		} catch (DisabledException ex) {
			ApiResponse<JwtResponse> response = new ApiResponse<>(false, "User is disabled!", null,
					HttpStatus.UNAUTHORIZED.value());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

		} catch (Exception ex) {
			ApiResponse<JwtResponse> response = new ApiResponse<>(false, ex.getLocalizedMessage(), null,
					HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

//	@GetMapping("/oauth-success")
//	public ResponseEntity<ApiResponse<JwtResponse>> handleOAuth2Login(@AuthenticationPrincipal OAuth2User oAuth2User) {
//		if (oAuth2User == null) {
//			ApiResponse<JwtResponse> errorResponse = new ApiResponse<>(false, "Not authenticated via OAuth2", null,
//					HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now());
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
//		}
//
//		try {
//			User user = userService.processOAuthPostLogin(oAuth2User);
//			String token = jwtUtil.generateToken(user.getEmail());
//
//			JwtResponse jwtResponse = new JwtResponse(token, "Bearer");
//			ApiResponse<JwtResponse> successResponse = new ApiResponse<>(true, "OAuth2 login successful", jwtResponse,
//					HttpStatus.OK.value(), LocalDateTime.now());
//			return ResponseEntity.ok(successResponse);
//		} catch (Exception e) {
//			ApiResponse<JwtResponse> errorResponse = new ApiResponse<>(false,
//					"Internal server error during OAuth2 login", null, HttpStatus.INTERNAL_SERVER_ERROR.value(),
//					LocalDateTime.now());
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//		}
//	}

	// Get current user details
	@GetMapping("/current-user")
	@TrackExecutionTime
	public ResponseEntity<ApiResponse<User>> getCurrentUser(Principal principal) {
		try {
			User user = (User) customUserDetailsService.loadUserByUsername(principal.getName());

			ApiResponse<User> response = new ApiResponse<>(true, "Current user fetched successfully", user,
					HttpStatus.OK.value(), LocalDateTime.now());

			return ResponseEntity.ok(response);

		} catch (UsernameNotFoundException ex) {
			ApiResponse<User> errorResponse = new ApiResponse<>(false, "User not found", null,
					HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

		} catch (Exception ex) {
			ApiResponse<User> errorResponse = new ApiResponse<>(false, ex.getLocalizedMessage(), null,
					HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("Username can not be empty!.");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("Password can not be empty!.");
            }
            
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("User Disabled" + e.getMessage());
		} catch (BadCredentialsException e) {
			throw new Exception("Invalid Credentials" + e.getMessage());
		}
	}

}
