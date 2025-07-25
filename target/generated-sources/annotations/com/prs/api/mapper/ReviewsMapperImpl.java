package com.prs.api.mapper;

import com.prs.api.entity.Reviews;
import com.prs.api.response.ReviewResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-22T22:26:39+0800",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.37.0.v20240215-1558, environment: Java 21.0.2 (Eclipse Adoptium)"
)
@Component
public class ReviewsMapperImpl implements ReviewsMapper {

    @Override
    public ReviewResponse toDto(Reviews review) {
        if ( review == null ) {
            return null;
        }

        ReviewResponse reviewResponse = new ReviewResponse();

        reviewResponse.setTagLines( mapTagLinesToStrings( review.getTagLines() ) );
        reviewResponse.setBadFlag( review.getBadFlag() );
        reviewResponse.setComment( review.getComment() );
        reviewResponse.setDislikes( review.getDislikes() );
        reviewResponse.setLikes( review.getLikes() );
        if ( review.getRating() != null ) {
            reviewResponse.setRating( review.getRating().intValue() );
        }
        reviewResponse.setRevId( review.getRevId() );
        reviewResponse.setSubject( review.getSubject() );
        reviewResponse.setSubmittedAt( review.getSubmittedAt() );

        reviewResponse.setReviewerName( MapperUtils.mapReviewerName(review.getPublicUser()) );

        return reviewResponse;
    }
}
