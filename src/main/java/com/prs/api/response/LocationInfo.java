package com.prs.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LocationInfo {
	
	public String ip;
	public String version;
	public String city;
	public String region;
	public String region_code;
	public String country_code;
	public String country_code_iso3;
	public String country_name;
	public String country_capital;
	public String country_tld;
	public String continent_code;
	public boolean in_eu;
	public String postal;
	public double latitude;
	public double longitude;
	public String timezone;
	public String utc_offset;
	public String country_calling_code;
	public String currency;
	public String currency_name;
	public String languages;
	public double country_area;
	public int country_population;
	public String asn;
	public String org;
	public String hostname;
}