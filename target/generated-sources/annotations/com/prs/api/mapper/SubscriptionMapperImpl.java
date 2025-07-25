package com.prs.api.mapper;

import com.prs.api.entity.Subscription;
import com.prs.api.response.dto.SubscriptionDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-22T22:26:39+0800",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.37.0.v20240215-1558, environment: Java 21.0.2 (Eclipse Adoptium)"
)
@Component
public class SubscriptionMapperImpl implements SubscriptionMapper {

    @Override
    public SubscriptionDto toDto(Subscription subscription) {
        if ( subscription == null ) {
            return null;
        }

        SubscriptionDto.SubscriptionDtoBuilder subscriptionDto = SubscriptionDto.builder();

        subscriptionDto.createdDate( subscription.getCreatedDate() );
        subscriptionDto.description( subscription.getDescription() );
        subscriptionDto.enabled( subscription.getEnabled() );
        subscriptionDto.planName( subscription.getPlanName() );
        subscriptionDto.price( subscription.getPrice() );
        subscriptionDto.subId( subscription.getSubId() );
        subscriptionDto.subscriptionEnd( subscription.getSubscriptionEnd() );
        subscriptionDto.subscriptionStart( subscription.getSubscriptionStart() );
        subscriptionDto.updatedDate( subscription.getUpdatedDate() );

        return subscriptionDto.build();
    }
}
