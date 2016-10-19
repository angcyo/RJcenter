package com.rsen.dialog.address;

import java.util.ArrayList;

public class Province {
	String region_id;
	String region_name;
	ArrayList<City> city = new ArrayList<City>();

	public String getRegion_id() {
		return region_id;
	}

	public void setRegion_id(String region_id) {
		this.region_id = region_id;
	}

	public String getRegion_name() {
		return region_name;
	}

	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}

	public ArrayList<City> getCity() {
		return city;
	}

	public void setCity(ArrayList<City> city) {
		this.city = city;
	}
}
