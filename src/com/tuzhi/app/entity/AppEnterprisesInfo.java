package com.tuzhi.app.entity;

/**
 * @Description: 
 * @company: 
 * @author: weifengnian
 * @Data: 2017年4月24日	
 * @Copyright:
 */
public class AppEnterprisesInfo {
	
	private int id; //
	private String name; //企业名
	private String login_name; //登录名
	private String password; //密码
	private String enterprise_url; //企业logo
	private String local_enterprise_url; //本地地址
	private String mobile_phone; //手机
	private String telephone; //联系电话
	private String create_time; //创建时间
	private String update_time; //修改时间
	private String business_code; //营业执照编号
	private String business_url; //营业执照
	private String local_business_url; //本地地址
	private int is_auth; //是否认证	0:未提交	1:已认证	10:认证中/待实名	-1:认证未通过(人工)	-2:认证未通过(机器)
	private String auth_create_time; //认证时间
	private String legal_person; //法人
	private String desc; //公司简介
	private String token; //令牌
	private int money; //余额
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
	public String getLogin_name() {
		return login_name;
	}
	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEnterprise_url() {
		return enterprise_url;
	}
	public void setEnterprise_url(String enterprise_url) {
		this.enterprise_url = enterprise_url;
	}
	public String getLocal_enterprise_url() {
		return local_enterprise_url;
	}
	public void setLocal_enterprise_url(String local_enterprise_url) {
		this.local_enterprise_url = local_enterprise_url;
	}
	public String getMobile_phone() {
		return mobile_phone;
	}
	public void setMobile_phone(String mobile_phone) {
		this.mobile_phone = mobile_phone;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getBusiness_code() {
		return business_code;
	}
	public void setBusiness_code(String business_code) {
		this.business_code = business_code;
	}
	public String getBusiness_url() {
		return business_url;
	}
	public void setBusiness_url(String business_url) {
		this.business_url = business_url;
	}
	public String getLocal_business_url() {
		return local_business_url;
	}
	public void setLocal_business_url(String local_business_url) {
		this.local_business_url = local_business_url;
	}
	public int getIs_auth() {
		return is_auth;
	}
	public void setIs_auth(int is_auth) {
		this.is_auth = is_auth;
	}
	public String getAuth_create_time() {
		return auth_create_time;
	}
	public void setAuth_create_time(String auth_create_time) {
		this.auth_create_time = auth_create_time;
	}
	public String getLegal_person() {
		return legal_person;
	}
	public void setLegal_person(String legal_person) {
		this.legal_person = legal_person;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
