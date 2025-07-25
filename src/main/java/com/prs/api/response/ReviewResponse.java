package com.prs.api.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ReviewResponse {
    private Long revId;
    private String reviewerName;
    private String subject;
    private String comment;
    private int rating;
    private String badFlag;
    private LocalDateTime submittedAt;
	private Integer likes;
	private Integer dislikes;
	private List<String> tagLines = new ArrayList<>();
}