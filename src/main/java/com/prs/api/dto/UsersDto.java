package com.prs.api.dto;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"userId","userCurrentStatus","joiningDate","profilePic","posterImg","dob"})
public class UsersDto {

	private Long userId;
	private String firstname;
	private String lastname;
	private String email;
	private String username;
	private String password;
	private String gender;

	private String city;
	private String state;
	private String country;
	private String zipcode;

	private String address;
	private String phoneNumber;

	private String dob;

	private String userCurrentStatus;
	private String joiningDate;
	private MultipartFile profilePic;
	private MultipartFile posterImg;

}
