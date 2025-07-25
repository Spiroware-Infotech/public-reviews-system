package com.prs.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.prs.api.entity.PublicUser;
import com.prs.api.response.dto.UserAllResponse;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface PublicUserMapper {

	@Mapping(source = "user.id", target = "userId")
	@Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.phoneNumber", target = "phoneNumber")
	@Mapping(source = "user.createTime", target = "createTime")
	@Mapping(source = "user.verificationCode", target = "verificationCode")
	UserAllResponse toDto(PublicUser user);
}
