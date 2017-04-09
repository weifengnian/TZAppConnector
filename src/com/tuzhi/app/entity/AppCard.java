package com.tuzhi.app.entity;

public class AppCard {
	
	private int id; //
	private String number; //身份证号码
	private String upper_url; //正面照片地址
	private String local_upper_url; //本地地址
	private String below_url; //反面照片地址
	private String local_below_url; //本地地址
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getUpper_url() {
		return upper_url;
	}
	public void setUpper_url(String upper_url) {
		this.upper_url = upper_url;
	}
	public String getLocal_upper_url() {
		return local_upper_url;
	}
	public void setLocal_upper_url(String local_upper_url) {
		this.local_upper_url = local_upper_url;
	}
	public String getBelow_url() {
		return below_url;
	}
	public void setBelow_url(String below_url) {
		this.below_url = below_url;
	}
	public String getLocal_below_url() {
		return local_below_url;
	}
	public void setLocal_below_url(String local_below_url) {
		this.local_below_url = local_below_url;
	}
}
