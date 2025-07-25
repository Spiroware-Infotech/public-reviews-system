package com.prs.api.service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeoLocationService {

	@Value("${google.maps.api.key}")
	private String apiKey;

	public Optional<double[]> getLatLongFromAddress(String address) {
		try {
			String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
			String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s",
					encodedAddress, apiKey);

			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			JSONObject json = new JSONObject(response.body());
			JSONArray results = json.getJSONArray("results");
			if (results.length() > 0) {
				JSONObject location = results.getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
				double lat = location.getDouble("lat");
				double lng = location.getDouble("lng");
				return Optional.of(new double[] { lat, lng });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}
	
	public Optional<double[]> getLatLongFromPincode(String pincode) {
        try {
            String url = String.format(
                "https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s",
                URLEncoder.encode(pincode, StandardCharsets.UTF_8), apiKey
            );

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject json = new JSONObject(response.body());
            JSONArray results = json.getJSONArray("results");

            if (!results.isEmpty()) {
                JSONObject location = results.getJSONObject(0)
                    .getJSONObject("geometry")
                    .getJSONObject("location");

                double lat = location.getDouble("lat");
                double lng = location.getDouble("lng");

                return Optional.of(new double[]{lat, lng});
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}