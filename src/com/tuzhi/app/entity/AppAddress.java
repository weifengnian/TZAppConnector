package com.tuzhi.app.entity;

/**
 * @Description: 
 * @company: 
 * @author: weifengnian
 * @Data: 2017年4月24日	
 * @Copyright:
 */
public class AppAddress {
	
	private int id; //
	private String pro_id; //省
	private String pro_name; //省名称
	private String city_id; //市
	private String city_name; //城市名
	private String dis_id; //区
	private String dis_name; //区名
	private String street_id; //街道
	private String street_name; //街道名
	private String details; //详细
	private float latitude; //纬度
	private float longitude; //经度
	private String only_id;  //唯一标识
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPro_id() {
		return pro_id;
	}
	public void setPro_id(String pro_id) {
		this.pro_id = pro_id;
	}
	public String getPro_name() {
		return pro_name;
	}
	public void setPro_name(String pro_name) {
		this.pro_name = pro_name;
	}
	public String getCity_id() {
		return city_id;
	}
	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public String getDis_id() {
		return dis_id;
	}
	public void setDis_id(String dis_id) {
		this.dis_id = dis_id;
	}
	public String getDis_name() {
		return dis_name;
	}
	public void setDis_name(String dis_name) {
		this.dis_name = dis_name;
	}
	public String getStreet_id() {
		return street_id;
	}
	public void setStreet_id(String street_id) {
		this.street_id = street_id;
	}
	public String getStreet_name() {
		return street_name;
	}
	public void setStreet_name(String street_name) {
		this.street_name = street_name;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public String getOnly_id() {
		return only_id;
	}
	public void setOnly_id(String only_id) {
		this.only_id = only_id;
	}
}
