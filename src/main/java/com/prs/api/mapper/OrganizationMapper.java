package com.prs.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.prs.api.entity.Organization;
import com.prs.api.response.dto.OrganizationResponse;

@Mapper(componentModel = "spring", uses = {SubscriptionMapper.class, CategoryMapper.class, ReviewsMapper.class })
public interface OrganizationMapper {

	@Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.phoneNumber", target = "phoneNumber")
	OrganizationResponse toDto(Organization organization);

}