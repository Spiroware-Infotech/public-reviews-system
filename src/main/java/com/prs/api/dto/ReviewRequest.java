package com.prs.api.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
	
	private Long userId;
	private Long organizationId;
    private String subject;
    private String comment;
    private Double rating;
    @JsonIgnore
    private String badFlag;
    private List<String> tagLines;
    private List<String> imagePaths;
	private LocalDateTime timestamp;
	
}