package com.prs.api.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.PropertyUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.prs.api.entity.Organization;
import com.prs.api.response.LocationInfo;
import com.prs.api.response.dto.CategoryDto;
import com.prs.api.response.dto.OrganizationResponse;
import com.prs.api.response.dto.SubscriptionDto;

public class DtoConverter {

	public static <T, U> U convertToDto(T source, Class<U> targetClass) {
		try {
			U target = targetClass.getDeclaredConstructor().newInstance();
			PropertyUtils.copyProperties(target, source);
			return target;
		} catch (Exception e) {
			throw new RuntimeException("Failed to convert to DTO: " + e.getMessage(), e);
		}
	}

	public static <T, S> List<S> convertListToDto(List<T> sourceList, Class<S> targetClass) {
		return sourceList.stream().map(source -> convertToDto(source, targetClass)).collect(Collectors.toList());
	}

	public static LocationInfo convertToLocationInfo(JsonObject jsonObject) {
		Gson gson = new Gson();
		return gson.fromJson(jsonObject, LocationInfo.class);
	}

	public static OrganizationResponse convertOrganizationToDto(Organization org) {
		OrganizationResponse dto = convertToDto(org, OrganizationResponse.class);

		if (org.getCategory() != null && org.getCategory().getName() != null) {
			dto.setCategory(convertToDto(org.getCategory(), CategoryDto.class));
		}

		if (org.getSubscription() != null && org.getSubscription().getPlanName() != null) {
			dto.setSubscription(convertToDto(org.getSubscription(), SubscriptionDto.class));
		}

		return dto;
	}

}