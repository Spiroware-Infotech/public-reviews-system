package com.admin.api;

import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jakarta.servlet.http.HttpServletRequest;

public class LocationServiceTest {

	public static void main(String[] args) {
	    // Example IP (you can also get it from HttpServletRequest)
	    String userIP = "116.14.162.30";
	    LocationServiceTest.getLocationFromIP(userIP);
	    
	}
	
    public static JsonObject getLocationFromIP(String ip) {
        String url = "https://ipwho.is/" + ip;

        RestTemplate restTemplate = new RestTemplate();
        String jsonResponse = restTemplate.getForObject(url, String.class);

        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

        if (jsonObject.get("success").getAsBoolean()) {
            double latitude = jsonObject.get("latitude").getAsDouble();
            double longitude = jsonObject.get("longitude").getAsDouble();
            System.out.println("Lat: " + latitude + ", Lng: " + longitude);
            return jsonObject;
        } else {
            System.out.println("Error: " + jsonObject.get("message").getAsString());
            return null;
        }
    }
    
    public String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        System.out.println("IP: "+xfHeader.split(",")[0]);
        return xfHeader == null ? request.getRemoteAddr() : xfHeader.split(",")[0];
    }
}