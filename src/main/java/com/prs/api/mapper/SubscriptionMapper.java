package com.prs.api.mapper;

import org.mapstruct.Mapper;

import com.prs.api.entity.Subscription;
import com.prs.api.response.dto.SubscriptionDto;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
	
    SubscriptionDto toDto(Subscription subscription);
}