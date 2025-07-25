package com.prs.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prs.api.aspects.TrackExecutionTime;
import com.prs.api.org.service.OrganizationService;
import com.prs.api.response.ApiResponse;
import com.prs.api.response.LocationInfo;
import com.prs.api.response.dto.OrganizationResponse;
import com.prs.api.service.LocationService;
import com.prs.api.utils.GeoUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/search")
@Tag(name = "Search REST API")
@RequiredArgsConstructor
public class SearchResultsRestApiController {

	@Value("${app.maxDistanceKm}")
	private double maxDistanceKm; // Example: only include orgs within 100 km
	
	private final OrganizationService organizationService;

	private final LocationService locationService;
	
	@TrackExecutionTime
	@GetMapping("/results")
	public ResponseEntity<ApiResponse<?>> searchOrganizations(@RequestParam String category) {

		try {
			LocationInfo locationInfo = locationService.getLocationFromIPForPaid(null);
			if(Objects.isNull(locationInfo)) {
				locationInfo =locationService.getLocationFromIPForFree(null);
			}
			
			List<OrganizationResponse> organizations = organizationService.findByCategoryName(category);

			if (organizations.isEmpty()) {
				ApiResponse<?> notFoundResponse = new ApiResponse<>(false, "No organizations found in category: " + category,
						new ArrayList<>(), HttpStatus.NOT_FOUND.value());
				return new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND);
			}

			double lat = locationInfo.getLatitude() ;
			double lng = locationInfo.getLongitude();
			
			List<OrganizationResponse> rankedOrganizations = organizations.stream()
				    .map(org -> new RankedOrganization(
				        org,
				        GeoUtils.calculateDistance(lat, lng, org.getLatitude(), org.getLongitude())
				    ))
				    // Only keep organizations within desired distance
				    .filter(ranked -> ranked.distance <= maxDistanceKm)
				    // Sort by: Subscription active > Higher rating > Closer distance
				    .sorted((o1, o2) -> {
				        if (o1.organization.isSubscriptionActive() && !o2.organization.isSubscriptionActive()) return -1;
				        if (!o1.organization.isSubscriptionActive() && o2.organization.isSubscriptionActive()) return 1;
				        int ratingCompare = Double.compare(
				            o2.organization.getAverageRating(),
				            o1.organization.getAverageRating()
				        );
				        return ratingCompare != 0 ? ratingCompare : Double.compare(o1.distance, o2.distance);
				    })
				    .map(RankedOrganization::toDTO)
				    .collect(Collectors.toList());

			if (rankedOrganizations.size() == 0) {
				ApiResponse<?> notFoundResponse = new ApiResponse<>(false, "No organizations found in category: " + category,
						null, HttpStatus.NOT_FOUND.value());
				return new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND);
			}
			ApiResponse<List<OrganizationResponse>> successResponse = new ApiResponse<>(true,
					"Organizations found successfully", rankedOrganizations, HttpStatus.OK.value());
			return ResponseEntity.ok(successResponse);

		} catch (Exception ex) {
			ApiResponse<?> errorResponse = new ApiResponse<>(false, "An error occurred: " + ex.getMessage(), null,
					HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	record RankedOrganization(OrganizationResponse organization, double distance) {

	    OrganizationResponse toDTO() {
	        // Set the distance inside the organization response if it's a field
	        organization.setDistance(distance);  // Make sure your DTO has a `distance` field with a setter
	        return organization;
	    }
	}
	
	

}
