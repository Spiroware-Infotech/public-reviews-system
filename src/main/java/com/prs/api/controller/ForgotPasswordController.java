package com.prs.api.controller;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prs.api.aspects.TrackExecutionTime;
import com.prs.api.config.Utility;
import com.prs.api.dto.ResetPasswordRequest;
import com.prs.api.entity.User;
import com.prs.api.exceptions.UserNotFountException;
import com.prs.api.response.ApiResponse;
import com.prs.api.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "ForgotPassword Rest API")
public class ForgotPasswordController {

	@Autowired
	private UserService userService;

	@Autowired
	private JavaMailSender javaMailSender;

	@TrackExecutionTime
	@PostMapping("/forgot-password")
	public ResponseEntity<ApiResponse<String>> processForgotPassword(@RequestParam String email,
			HttpServletRequest request) {
		try {
			String token = RandomString.make(45);

			userService.updateResetPasswordToken(token, email);

			String resetPasswordLink = Utility.getSiteUrl(request) + "/reset_password?token=" + token;
			// You may want to log this, but avoid printing sensitive info in production
			log.info(resetPasswordLink);

			sendEmail(email, resetPasswordLink);

			ApiResponse<String> response = new ApiResponse<>(true, "Password reset link sent. Please check your email.",
					null, HttpStatus.OK.value(), LocalDateTime.now());
			return ResponseEntity.ok(response);

		} catch (UserNotFountException ex) {
			ApiResponse<String> errorResponse = new ApiResponse<>(false, ex.getMessage(), null,
					HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

		} catch (UnsupportedEncodingException | MessagingException ex) {
			ApiResponse<String> errorResponse = new ApiResponse<>(false, "Failed to send reset email.", null,
					HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);

		} catch (Exception ex) {
			ApiResponse<String> errorResponse = new ApiResponse<>(false, ex.getLocalizedMessage(), null,
					HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	// sending email with reset password token
	private void sendEmail(String email, String resetPasswordLink)
			throws MessagingException, UnsupportedEncodingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("examportal947@gmail.com", "Public Review System Support");
		helper.setTo(email);

		String subject = "Here's the link to reset your password";

		String content = "<p>Hello,</p>" + "<p>You have requested to reset your password.</p>"
				+ "<p>Click the link below to change your password:</p>" + "<p><a href=\"" + resetPasswordLink
				+ "\">Change my password</a></p>" + "<br>" + "<p>Ignore this email if you do remember your password, "
				+ "or you have not made the request.</p>";

		helper.setSubject(subject);

		helper.setText(content, true);

		javaMailSender.send(message);
	}

	@TrackExecutionTime
	@GetMapping("/reset_password")
	public ResponseEntity<ApiResponse<String>> showResetPasswordForm(@RequestParam("token") String token) {
		User user = userService.getResetPasswordToken(token);

		if (user == null) {
			ApiResponse<String> errorResponse = new ApiResponse<>(false, "Invalid or expired token.", null,
					HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}

		ApiResponse<String> successResponse = new ApiResponse<>(true,
				"Token verified. You may proceed to reset your password.", "VALID_TOKEN", HttpStatus.OK.value(),
				LocalDateTime.now());
		return ResponseEntity.ok(successResponse);
	}

	@TrackExecutionTime
	@PostMapping("/reset_password")
	public ResponseEntity<ApiResponse<String>> processResetPassword(@RequestBody ResetPasswordRequest resetRequest) {
		String token = resetRequest.getToken();
		String password = resetRequest.getPassword();

		User user = userService.getResetPasswordToken(token);

		if (user == null) {
			ApiResponse<String> errorResponse = new ApiResponse<>(false, "Invalid or expired token", null,
					HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}

		userService.updatePassword(user, password);

		ApiResponse<String> successResponse = new ApiResponse<>(true, "Password changed successfully", null,
				HttpStatus.OK.value(), LocalDateTime.now());
		return ResponseEntity.ok(successResponse);
	}
}
