package com.prs.api.dto;

public record LocationData(
    String place_id,
    String lat,
    String lon,
    String display_name
    // Add other fields as needed
) {}