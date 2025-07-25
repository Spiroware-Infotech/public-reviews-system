package com.prs.api.mapper;

import com.prs.api.entity.PublicUser;

public class MapperUtils {

    public static String mapReviewerName(PublicUser user) {
        return (user != null && user.getFirstname() != null) ? user.getFirstname() : "Anonymous";
    }
}