package com.prs.api.org.service;

import java.util.List;

import com.prs.api.response.dto.OrganizationResponse;

public interface OrganizationService {

	OrganizationResponse getOrganizationById(Long id);

	List<OrganizationResponse> findByCategoryName(String category);

}
