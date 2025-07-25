package com.prs.api.mapper;

import com.prs.api.entity.User;
import com.prs.api.response.dto.UserResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-22T22:26:39+0800",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.37.0.v20240215-1558, environment: Java 21.0.2 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponse toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.createTime( user.getCreateTime() );
        userResponse.email( user.getEmail() );
        userResponse.firstname( user.getFirstname() );
        userResponse.lastname( user.getLastname() );
        userResponse.password( user.getPassword() );
        userResponse.phoneNumber( user.getPhoneNumber() );
        userResponse.resetPasswordToken( user.getResetPasswordToken() );
        userResponse.userId( user.getUserId() );
        userResponse.username( user.getUsername() );
        userResponse.verificationCode( user.getVerificationCode() );

        return userResponse.build();
    }
}
