package com.prs.api.response.dto;

import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDto {

	private Long subId;

	private String planName;
	private String description;
	private Double price;
	private LocalDate subscriptionStart;
	private LocalDate subscriptionEnd;
	private Date createdDate;
	private Date updatedDate;

	private Boolean enabled;
}
