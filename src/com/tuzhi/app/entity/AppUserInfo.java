package com.tuzhi.app.entity;

import java.util.Date;

public class AppUserInfo {
	
	private int id; //
	private String name; //真实名
	private String nick_name; //昵称
	private String password; //密码
	private String icon_url; //头像
	private String local_url; //本地地址
	private String mobile_phone; //手机
	private int sex; //性别	1:男	2:女
	private Date create_time; //创建时间
	private Date update_time; //修改时间
	private int integral; //积分
	private int card_id; //关联身份证
	private Date graduation_time; //毕业时间
	private String address_id; //所在地址
	private String token; //令牌
	private int level_id; //关联等级
	private int is_auth; //是否实名认证	0:未提交	1:已认证	10:认证中/待实名	-1:认证未通过(人工)	-2:认证未通过(机器)
	private Date doubleauth_create_time; //认证时间
	private double money; //帐号金额
	private int status; //状态	-1:黑名单	1:正常
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNick_name() {
		return nick_name;
	}
	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getIcon_url() {
		return icon_url;
	}
	public void setIcon_url(String icon_url) {
		this.icon_url = icon_url;
	}
	public String getLocal_url() {
		return local_url;
	}
	public void setLocal_url(String local_url) {
		this.local_url = local_url;
	}
	public String getMobile_phone() {
		return mobile_phone;
	}
	public void setMobile_phone(String mobile_phone) {
		this.mobile_phone = mobile_phone;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
	public int getIntegral() {
		return integral;
	}
	public void setIntegral(int integral) {
		this.integral = integral;
	}
	public int getCard_id() {
		return card_id;
	}
	public void setCard_id(int card_id) {
		this.card_id = card_id;
	}
	public Date getGraduation_time() {
		return graduation_time;
	}
	public void setGraduation_time(Date graduation_time) {
		this.graduation_time = graduation_time;
	}
	public String getAddress_id() {
		return address_id;
	}
	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getLevel_id() {
		return level_id;
	}
	public void setLevel_id(int level_id) {
		this.level_id = level_id;
	}
	public int getIs_auth() {
		return is_auth;
	}
	public void setIs_auth(int is_auth) {
		this.is_auth = is_auth;
	}
	public Date getDoubleauth_create_time() {
		return doubleauth_create_time;
	}
	public void setDoubleauth_create_time(Date doubleauth_create_time) {
		this.doubleauth_create_time = doubleauth_create_time;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
