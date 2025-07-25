package com.prs.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDto {

	private String orgName;
	private String firstname;
	private String lastname;
	
	private String email;
	private String username;
	private String password;
	
	private String address;
	private String city;
	private String state;
	private String country;
	private String phoneNumber;
	private String pincode;
	private String description;
	private String websitelink;
	private String youtubelink;
	private String instalink;
	private String xlink;
	private String fblink;
	
	private Long catId;
	private Long subId;
}
