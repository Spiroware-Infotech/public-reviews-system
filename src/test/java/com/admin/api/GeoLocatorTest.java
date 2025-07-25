package com.admin.api;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class GeoLocatorTest {

    public static void main(String[] args) throws Exception {
        String ip = getPublicIP(); // you can get client IP from request
        System.out.println("Public IP: " +ip);
        String url = "https://ipapi.co/" + ip + "/json/";

        HttpURLConnection request = (HttpURLConnection) new URL(url).openConnection();
        request.connect();

        JsonObject json = JsonParser.parseReader(new InputStreamReader(request.getInputStream())).getAsJsonObject();

        double latitude = json.get("latitude").getAsDouble();
        double longitude = json.get("longitude").getAsDouble();

        System.out.println("Latitude: " + latitude + ", Longitude: " + longitude);
        
        
        InetAddress localHost = InetAddress.getLocalHost();
        System.out.println("Local Host IP: " + localHost.getHostAddress());
        
        InetAddress googleDNS = InetAddress.getByName(ip);
        System.out.println("Google DNS IP: " + googleDNS.getHostAddress());
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
