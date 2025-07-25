package com.prs.api.response.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

	private Long userId;
	private String firstname;
	private String lastname;
	private String email;
	private String username;
	@JsonIgnore
	private String password;
	private String phoneNumber;
	private boolean isActive;
	private String verificationCode;
	private String resetPasswordToken;
	private Date createTime;
	private String role;
}
