package com.example.bmobexample.bean;

import java.util.List;

import org.json.JSONObject;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;

public class Person extends BmobObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private Integer age;
	private Boolean gender;
	private String address;
	private String email;
	
	private BankCard bankCard;		// 对应服务端的Object类型
	private List<String> hobby;		// 对应服务端Array类型：String类型的集合
	private List<BankCard> cards;	// 对应服务端Array类型:Object类型的集合
	List<JSONObject> jsons;			//json
	
	private BmobFile pic;
	private BmobGeoPoint gpsAdd;
	private BmobDate uploadTime;
	
	private MyUser author;
	
	public List<JSONObject> getJsons() {
		return jsons;
	}
	public void setJsons(List<JSONObject> jsons) {
		this.jsons = jsons;
	}
	public MyUser getAuthor() {
		return author;
	}
	public void setAuthor(MyUser author) {
		this.author = author;
	}
	public Boolean getGender() {
		return gender;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Boolean isGender() {
		return gender;
	}
	public void setGender(Boolean gender) {
		this.gender = gender;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public BmobFile getPic() {
		return pic;
	}
	public void setPic(BmobFile pic) {
		this.pic = pic;
	}
	public BmobGeoPoint getGpsAdd() {
		return gpsAdd;
	}
	public void setGpsAdd(BmobGeoPoint gpsAdd) {
		this.gpsAdd = gpsAdd;
	}
	public BmobDate getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(BmobDate uploadTime) {
		this.uploadTime = uploadTime;
	}
	public BankCard getBankCard() {
		return bankCard;
	}
	public void setBankCard(BankCard bankCard) {
		this.bankCard = bankCard;
	}
	public List<String> getHobby() {
		return hobby;
	}
	public void setHobby(List<String> hobby) {
		this.hobby = hobby;
	}
	public List<BankCard> getCards() {
		return cards;
	}
	public void setCards(List<BankCard> cards) {
		this.cards = cards;
	}
	
}
