package com.prs.api.org.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prs.api.entity.Organization;
import com.prs.api.mapper.OrganizationMapper;
import com.prs.api.org.repository.OrganizationRepository;
import com.prs.api.response.dto.OrganizationResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

	private final OrganizationRepository organizationRepository;

	private final OrganizationMapper organizationMapper;

	@Override
	public OrganizationResponse getOrganizationById(Long id) {
		Organization organization = organizationRepository.findOrganizationById(id);
		// convert entity object to response dto
		OrganizationResponse orgResponse = organizationMapper.toDto(organization);

		return orgResponse;
	}

	@Override
	public List<OrganizationResponse> findByCategoryName(String category) {
		List<Organization> organizationList = organizationRepository.findByCategoryName(category);
		// Convert List<Organization> to List<OrganizationResponse>
		List<OrganizationResponse> responseList = organizationList.stream().map(organizationMapper::toDto)
				.collect(Collectors.toList());
		return responseList;
	}

}
