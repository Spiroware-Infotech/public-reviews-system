package com.prs.api.mapper;

import com.prs.api.entity.Organization;
import com.prs.api.entity.Reviews;
import com.prs.api.entity.User;
import com.prs.api.response.ReviewResponse;
import com.prs.api.response.dto.OrganizationResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-22T22:26:39+0800",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.37.0.v20240215-1558, environment: Java 21.0.2 (Eclipse Adoptium)"
)
@Component
public class OrganizationMapperImpl implements OrganizationMapper {

    @Autowired
    private SubscriptionMapper subscriptionMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ReviewsMapper reviewsMapper;

    @Override
    public OrganizationResponse toDto(Organization organization) {
        if ( organization == null ) {
            return null;
        }

        OrganizationResponse.OrganizationResponseBuilder organizationResponse = OrganizationResponse.builder();

        organizationResponse.email( organizationUserEmail( organization ) );
        organizationResponse.username( organizationUserUsername( organization ) );
        organizationResponse.phoneNumber( organizationUserPhoneNumber( organization ) );
        organizationResponse.address( organization.getAddress() );
        organizationResponse.category( categoryMapper.toDto( organization.getCategory() ) );
        organizationResponse.city( organization.getCity() );
        organizationResponse.country( organization.getCountry() );
        organizationResponse.createDate( organization.getCreateDate() );
        organizationResponse.description( organization.getDescription() );
        organizationResponse.dislikes( organization.getDislikes() );
        organizationResponse.fblink( organization.getFblink() );
        organizationResponse.firstname( organization.getFirstname() );
        organizationResponse.id( organization.getId() );
        organizationResponse.instalink( organization.getInstalink() );
        organizationResponse.lastname( organization.getLastname() );
        organizationResponse.latitude( organization.getLatitude() );
        organizationResponse.likes( organization.getLikes() );
        organizationResponse.longitude( organization.getLongitude() );
        organizationResponse.orgName( organization.getOrgName() );
        organizationResponse.orgProfilePic( organization.getOrgProfilePic() );
        organizationResponse.pincode( organization.getPincode() );
        organizationResponse.reviews( reviewsListToReviewResponseList( organization.getReviews() ) );
        organizationResponse.sharing( organization.getSharing() );
        organizationResponse.state( organization.getState() );
        organizationResponse.subscription( subscriptionMapper.toDto( organization.getSubscription() ) );
        organizationResponse.updatedDate( organization.getUpdatedDate() );
        organizationResponse.websitelink( organization.getWebsitelink() );
        organizationResponse.xlink( organization.getXlink() );
        organizationResponse.youtubelink( organization.getYoutubelink() );

        return organizationResponse.build();
    }

    private String organizationUserEmail(Organization organization) {
        if ( organization == null ) {
            return null;
        }
        User user = organization.getUser();
        if ( user == null ) {
            return null;
        }
        String email = user.getEmail();
        if ( email == null ) {
            return null;
        }
        return email;
    }

    private String organizationUserUsername(Organization organization) {
        if ( organization == null ) {
            return null;
        }
        User user = organization.getUser();
        if ( user == null ) {
            return null;
        }
        String username = user.getUsername();
        if ( username == null ) {
            return null;
        }
        return username;
    }

    private String organizationUserPhoneNumber(Organization organization) {
        if ( organization == null ) {
            return null;
        }
        User user = organization.getUser();
        if ( user == null ) {
            return null;
        }
        String phoneNumber = user.getPhoneNumber();
        if ( phoneNumber == null ) {
            return null;
        }
        return phoneNumber;
    }

    protected List<ReviewResponse> reviewsListToReviewResponseList(List<Reviews> list) {
        if ( list == null ) {
            return null;
        }

        List<ReviewResponse> list1 = new ArrayList<ReviewResponse>( list.size() );
        for ( Reviews reviews : list ) {
            list1.add( reviewsMapper.toDto( reviews ) );
        }

        return list1;
    }
}
