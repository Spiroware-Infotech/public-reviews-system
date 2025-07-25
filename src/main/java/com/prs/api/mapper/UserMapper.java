package com.prs.api.mapper;

import org.mapstruct.Mapper;

import com.prs.api.entity.User;
import com.prs.api.response.dto.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {

	UserResponse toDto(User user);
}
