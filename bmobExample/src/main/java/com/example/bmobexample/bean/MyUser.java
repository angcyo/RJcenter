package com.example.bmobexample.bean;

import java.util.List;

import org.json.JSONObject;

import cn.bmob.v3.BmobUser;

public class MyUser extends BmobUser {
	
	/**  
	 *  
	 */  
	
	private static final long serialVersionUID = 1L;
	
	private Integer age;
	
	private List<String> goodAt;
	private List<JSONObject> notice;

	public List<String> getGoodAt() {
		return goodAt;
	}

	public void setGoodAt(List<String> goodAt) {
		this.goodAt = goodAt;
	}

	public List<JSONObject> getNotice() {
		return notice;
	}

	public void setNotice(List<JSONObject> notice) {
		this.notice = notice;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
}
