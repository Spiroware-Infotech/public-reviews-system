package com.prs.api.config;

import jakarta.servlet.http.HttpServletRequest;

public class Utility {

    public static String getSiteUrl(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        String tmp = siteURL.replace(request.getServletPath(), "");

        return tmp.replace("8080", "3000");
    }
}
