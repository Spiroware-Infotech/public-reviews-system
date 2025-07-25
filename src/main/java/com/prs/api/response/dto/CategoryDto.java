package com.prs.api.response.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
	
    private Long id;
    private String name;
	private String desciption;
	private Date createDate;
	private Boolean enabled;
}