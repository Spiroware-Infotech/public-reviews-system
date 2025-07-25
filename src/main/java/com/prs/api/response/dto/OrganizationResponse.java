package com.prs.api.response.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prs.api.response.ReviewResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationResponse {

	private Long id;
	private String orgName;
	private String firstname;
	private String lastname;

	private String email;
	private String username;
	@JsonIgnore
	private String password;

	private String address;
	private String pincode;
	private String phoneNumber;
	private String city;
	private String state;
	private String country;
	private double latitude;
	private double longitude;
	private String orgProfilePic;
	private Integer likes;
	private Integer dislikes;
	private String description;
	private Integer sharing;
	private Date createDate;
	private Date updatedDate;
	private String websitelink;
	private String youtubelink;
	private String instalink;
	private String xlink;
	private String fblink;

	private SubscriptionDto subscription;
	
	private CategoryDto category;
    private double averageRating;
    private double distance;

    private List<ReviewResponse> reviews = new ArrayList<>();;
    
    public boolean isSubscriptionActive() {
        LocalDate today = LocalDate.now();
        return subscription.getSubscriptionStart() != null && subscription.getSubscriptionEnd() != null
               && !today.isBefore(subscription.getSubscriptionStart())
               && !today.isAfter(subscription.getSubscriptionEnd());
    }
}
