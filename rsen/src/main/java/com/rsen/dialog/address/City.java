package com.rsen.dialog.address;

import java.util.ArrayList;

public class City {
	private String region_id;
	private String region_name;
	private ArrayList<County> dict = new ArrayList<County>();

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

	public ArrayList<County> getDict() {
		return dict;
	}

	public void setDict(ArrayList<County> dict) {
		this.dict = dict;
	}
}
