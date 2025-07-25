package com.prs.api.service;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.prs.api.dto.LocationData;
import com.prs.api.mapper.UserMapper;
import com.prs.api.response.LocationInfo;
import com.prs.api.utils.DtoConverter;

import jakarta.servlet.http.HttpServletRequest;
import reactor.core.publisher.Mono;

@Service
public class LocationService {

	@Autowired
	UserMapper locationInfoMapper;
	
    private final WebClient webClient;

    public LocationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://nominatim.openstreetmap.org").build();
    }

    public Mono<LocationData> getLocationByPincode(String pincode) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("postalcode", pincode)
                        //.queryParam("country", "India")
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .bodyToMono(LocationData[].class)
                .flatMap(response -> {
                    if (response.length > 0) {
                        return Mono.just(response[0]); // Return first result
                    }
                    return Mono.error(new RuntimeException("No location found"));
                });
    }
    
	public LocationInfo getLocationFromIPForFree(String ip) throws Exception {
		if (StringUtils.isBlank(ip)) {
			ip = getPublicIP();
		}

		String url = "https://ipwho.is/" + ip;

		RestTemplate restTemplate = new RestTemplate();
		String jsonResponse = restTemplate.getForObject(url, String.class);

		JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

		if (jsonObject.get("success").getAsBoolean()) {
			double latitude = jsonObject.get("latitude").getAsDouble();
			double longitude = jsonObject.get("longitude").getAsDouble();
			//System.out.println("Lat: " + latitude + ", Lng: " + longitude);
			LocationInfo location = DtoConverter.convertToLocationInfo(jsonObject);
			return location;
		} else {
			System.out.println("Error: " + jsonObject.get("message").getAsString());
			return null;
		}
	}
    
	public LocationInfo getLocationFromIPForPaid(String ip) throws Exception {

		if (StringUtils.isBlank(ip)) {
			ip = getPublicIP();
		}
		
		String url = "https://ipapi.co/" + ip + "/json/";

		HttpURLConnection request = (HttpURLConnection) new URL(url).openConnection();
		request.connect();

		JsonObject jsonObject = JsonParser.parseReader(new InputStreamReader(request.getInputStream()))
				.getAsJsonObject();

		double latitude = jsonObject.get("latitude").getAsDouble();
		double longitude = jsonObject.get("longitude").getAsDouble();

		LocationInfo location = DtoConverter.convertToLocationInfo(jsonObject);
		//System.out.println("Latitude: " + latitude + ", Longitude: " + longitude);
		return location;
	}
    
    public String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        System.out.println("IP: "+xfHeader.split(",")[0]);
        return xfHeader == null ? request.getRemoteAddr() : xfHeader.split(",")[0];
    }
    
    public static String getPublicIP() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.ipify.org?format=text"))
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}