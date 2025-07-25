package com.prs.api.mapper;

import com.prs.api.entity.PublicUser;
import com.prs.api.entity.User;
import com.prs.api.response.dto.UserAllResponse;
import java.util.Arrays;
import java.util.Date;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-22T22:26:39+0800",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.37.0.v20240215-1558, environment: Java 21.0.2 (Eclipse Adoptium)"
)
@Component
public class PublicUserMapperImpl implements PublicUserMapper {

    @Override
    public UserAllResponse toDto(PublicUser user) {
        if ( user == null ) {
            return null;
        }

        UserAllResponse.UserAllResponseBuilder userAllResponse = UserAllResponse.builder();

        userAllResponse.userId( user.getId() );
        userAllResponse.email( userUserEmail( user ) );
        userAllResponse.username( userUserUsername( user ) );
        userAllResponse.phoneNumber( userUserPhoneNumber( user ) );
        userAllResponse.createTime( userUserCreateTime( user ) );
        userAllResponse.verificationCode( userUserVerificationCode( user ) );
        userAllResponse.address( user.getAddress() );
        userAllResponse.bloodgroup( user.getBloodgroup() );
        userAllResponse.city( user.getCity() );
        userAllResponse.country( user.getCountry() );
        userAllResponse.currentStatus( user.getCurrentStatus() );
        userAllResponse.dob( user.getDob() );
        userAllResponse.firstname( user.getFirstname() );
        userAllResponse.gender( user.getGender() );
        userAllResponse.lastUpdateddate( user.getLastUpdateddate() );
        userAllResponse.lastname( user.getLastname() );
        userAllResponse.phone( user.getPhone() );
        byte[] profileImg = user.getProfileImg();
        if ( profileImg != null ) {
            userAllResponse.profileImg( Arrays.copyOf( profileImg, profileImg.length ) );
        }
        userAllResponse.religion( user.getReligion() );
        userAllResponse.remarks( user.getRemarks() );
        userAllResponse.state( user.getState() );
        userAllResponse.zipcode( user.getZipcode() );

        return userAllResponse.build();
    }

    private String userUserEmail(PublicUser publicUser) {
        if ( publicUser == null ) {
            return null;
        }
        User user = publicUser.getUser();
        if ( user == null ) {
            return null;
        }
        String email = user.getEmail();
        if ( email == null ) {
            return null;
        }
        return email;
    }

    private String userUserUsername(PublicUser publicUser) {
        if ( publicUser == null ) {
            return null;
        }
        User user = publicUser.getUser();
        if ( user == null ) {
            return null;
        }
        String username = user.getUsername();
        if ( username == null ) {
            return null;
        }
        return username;
    }

    private String userUserPhoneNumber(PublicUser publicUser) {
        if ( publicUser == null ) {
            return null;
        }
        User user = publicUser.getUser();
        if ( user == null ) {
            return null;
        }
        String phoneNumber = user.getPhoneNumber();
        if ( phoneNumber == null ) {
            return null;
        }
        return phoneNumber;
    }

    private Date userUserCreateTime(PublicUser publicUser) {
        if ( publicUser == null ) {
            return null;
        }
        User user = publicUser.getUser();
        if ( user == null ) {
            return null;
        }
        Date createTime = user.getCreateTime();
        if ( createTime == null ) {
            return null;
        }
        return createTime;
    }

    private String userUserVerificationCode(PublicUser publicUser) {
        if ( publicUser == null ) {
            return null;
        }
        User user = publicUser.getUser();
        if ( user == null ) {
            return null;
        }
        String verificationCode = user.getVerificationCode();
        if ( verificationCode == null ) {
            return null;
        }
        return verificationCode;
    }
}
