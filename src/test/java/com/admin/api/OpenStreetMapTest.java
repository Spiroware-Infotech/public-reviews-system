package com.admin.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class OpenStreetMapTest {

	public static void main(String[] args) throws UnsupportedEncodingException {
		String address = "Bangalore, Karnataka, India";
		String url = "https://nominatim.openstreetmap.org/search?q=" + URLEncoder.encode(address, "UTF-8") + "&format=json";
		
		System.out.println("URL : "+url);
		
		String pincode = "560075";
		String pinURL = "https://nominatim.openstreetmap.org/search?postalcode=" + pincode + "&country=India&format=json";

		System.out.println("URL-2: "+pinURL);
		
        
	}

}
