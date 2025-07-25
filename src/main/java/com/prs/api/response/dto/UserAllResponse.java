package com.prs.api.response.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAllResponse {

	// From User
	private Long userId;
	private String firstname;
	private String lastname;
	private String email;
	private String username;
	private String phoneNumber;
	
	@JsonIgnore
	private String password;
	private boolean isActive;
	private String verificationCode;
	private String resetPasswordToken;
	private Date createTime;
	private String role;
	private String gender;
	private String city;
	private String state;
	private String country;
	private String zipcode;
	private String address;
	private String phone;
	private String currentStatus;
	private Date lastUpdateddate;
	private Date dob;
	private String bloodgroup;
	private String remarks;
	@Lob
	private byte[] profileImg;
	private String religion;
}
